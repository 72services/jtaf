package ch.jtaf.boundary.controller

import ch.jtaf.boundary.dto.Message
import ch.jtaf.boundary.web.HttpContentProducer
import ch.jtaf.control.repository.*
import ch.jtaf.entity.Series
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Controller
class SeriesController(private val seriesRepository: SeriesRepository,
                       private val categoryRepository: CategoryRepository,
                       private val athleteRepository: AthleteRepository,
                       private val resultRepository: ResultRepository,
                       private val organizationRepository: OrganizationRepository) {

    val httpContentProducer = HttpContentProducer()

    @GetMapping("/sec/{organizationKey}/series")
    fun get(@PathVariable("organizationKey") organizationKey: String): ModelAndView {
        val mav = ModelAndView("/sec/series")
        mav.model["series"] = Series()

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/sec/{organizationKey}/series/{seriesId}")
    fun getById(@PathVariable("organizationKey") organizationKey: String,
                @PathVariable("seriesId") seriesId: Long): ModelAndView {
        val mav = ModelAndView("/sec/series")
        mav.model["series"] = seriesRepository.getOne(seriesId)
        mav.model["categories"] = categoryRepository.findAllBySeriesId(seriesId)
        mav.model["athletes"] = athleteRepository.findAthleteDTOsBySeriesId(seriesId)

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/series/{seriesId}/logo", produces = [MediaType.IMAGE_PNG_VALUE])
    fun getSeriesImage(@PathVariable seriesId: Long?): HttpEntity<*> {
        val series = seriesRepository.getOne(seriesId!!)

        if (series.logo != null && series.logo!!.isNotEmpty()) {
            var bufferedImage = ImageIO.read(ByteArrayInputStream(series.logo!!))
            if (bufferedImage != null) {
                bufferedImage = scaleImageByFixedHeight(bufferedImage, BufferedImage.TYPE_INT_RGB, 60)
                ByteArrayOutputStream().use { baos ->
                    ImageIO.write(bufferedImage, "png", baos)
                    return httpContentProducer.getContentAsPng("logo.png", baos.toByteArray())
                }
            }
        }
        return ResponseEntity.EMPTY
    }

    @GetMapping("/sec/{organizationKey}/series/{seriesId}/athlete/{athleteId}")
    fun addEvent(@PathVariable("organizationKey") organizationKey: String,
                 @PathVariable("seriesId") seriesId: Long, @PathVariable("athleteId") athleteId: Long): ModelAndView {
        val mav = ModelAndView("/sec/series")

        val series = seriesRepository.getOne(seriesId)
        val athlete = athleteRepository.getOne(athleteId)

        val category = categoryRepository.findBySeriesIdAndGenderAndYearFromLessThanEqualAndYearToGreaterThanEqual(
                seriesId, athlete.gender, athlete.yearOfBirth, athlete.yearOfBirth)

        if (category == null) {
            mav.model["athletes_message"] = "No matching category found for gender " + athlete.gender + " and year " + athlete.yearOfBirth
        } else {
            category.athletes.add(athlete)
            categoryRepository.save(category)
        }

        mav.model["series"] = series
        mav.model["categories"] = categoryRepository.findAllBySeriesId(seriesId)
        mav.model["athletes"] = athleteRepository.findAthleteDTOsBySeriesId(seriesId)

        mav.model["message"] = null
        return mav
    }

    @Transactional
    @GetMapping("/sec/{organizationKey}/series/{seriesId}/athlete/{athleteId}/delete")
    fun deleteById(@PathVariable("organizationKey") organizationKey: String,
                   @PathVariable("seriesId") seriesId: Long, @PathVariable("athleteId") athleteId: Long): ModelAndView {
        val mav = ModelAndView("/sec/series")

        val series = seriesRepository.getOne(seriesId)
        val athlete = athleteRepository.getOne(athleteId)

        val categories = categoryRepository.findByAthletesId(athleteId)

        categories.forEach {
            it.athletes.remove(athlete)
            categoryRepository.save(it)

            resultRepository.deleteResultsByCategoryIdAndAthleteId(it.id, athleteId)
        }

        mav.model["series"] = series
        mav.model["categories"] = categoryRepository.findAllBySeriesId(seriesId)
        mav.model["athletes"] = athleteRepository.findAthleteDTOsBySeriesId(seriesId)

        mav.model["message"] = null
        return mav
    }


    @PostMapping("/sec/{organizationKey}/series")
    fun post(@AuthenticationPrincipal user: User,
             @PathVariable("organizationKey") organizationKey: String,
             series: Series): ModelAndView {
        val mav = ModelAndView("/sec/series")

        val organization = organizationRepository.findByKey(organizationKey)

        if (series.id == null) {
            series.organizationId = organization.id
            seriesRepository.save(series)

            mav.model["series"] = series
        } else {
            val seriesFromDb = seriesRepository.getOne(series.id!!)
            seriesFromDb.name = series.name
            seriesFromDb.locked = series.locked
            seriesFromDb.hidden = series.hidden
            seriesFromDb.organizationId = organization.id
            seriesRepository.save(seriesFromDb)

            mav.model["series"] = seriesFromDb
        }

        mav.model["categories"] = categoryRepository.findAllBySeriesId(series.id!!)
        mav.model["athletes"] = athleteRepository.findAthleteDTOsBySeriesId(series.id!!)

        mav.model["message"] = Message(Message.success, "Series saved!")
        return mav
    }

    @GetMapping("/sec/{organizationKey}/series/{seriesId}/delete")
    fun deleteById(@AuthenticationPrincipal user: User,
                   @PathVariable("organizationKey") organizationKey: String,
                   @PathVariable("seriesId") seriesId: Long): ModelAndView {
        seriesRepository.deleteById(seriesId)

        val mav = ModelAndView("/sec/serieslist")

        val organization = organizationRepository.findByKey(organizationKey)
        mav.model["seriesList"] = seriesRepository.findByOrganizationId(organization.id!!)

        mav.model["message"] = null
        return mav
    }

    protected fun scaleImageByFixedHeight(image: BufferedImage, imageType: Int, newHeight: Int): BufferedImage {
        val ratio = image.getWidth(null).toDouble() / image.getHeight(null).toDouble()
        val newWidth = (ratio * newHeight).toInt()
        val scaled = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)
        val newImage = BufferedImage(newWidth, newHeight, imageType)
        val g = newImage.graphics
        g.drawImage(scaled, 0, 0, null)
        g.dispose()
        return newImage
    }
}
