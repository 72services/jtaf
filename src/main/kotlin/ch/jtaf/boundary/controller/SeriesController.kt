package ch.jtaf.boundary.controller

import ch.jtaf.boundary.dto.Message
import ch.jtaf.control.repository.*
import ch.jtaf.entity.Series
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class SeriesController(private val seriesRepository: SeriesRepository,
                       private val categoryRepository: CategoryRepository,
                       private val athleteRepository: AthleteRepository,
                       private val resultRepository: ResultRepository,
                       private val organizationRepository: OrganizationRepository) {

    @GetMapping("/sec/{organization}/series")
    fun get(@PathVariable("organization") organizationKey: String): ModelAndView {
        val mav = ModelAndView("/sec/series")
        mav.model["series"] = Series()

        mav.model["message"] = null
        return mav
    }

    @GetMapping("sec/{organization}/series/{seriesId}")
    fun getById(@PathVariable("organization") organizationKey: String,
                @PathVariable("seriesId") seriesId: Long): ModelAndView {
        val mav = ModelAndView("/sec/series")
        mav.model["series"] = seriesRepository.getOne(seriesId)
        mav.model["categories"] = categoryRepository.findAllBySeriesId(seriesId)
        mav.model["athletes"] = athleteRepository.findAthleteDTOsBySeriesId(seriesId)

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/sec/{organization}/series/{id}/athlete/{athleteId}")
    fun addEvent(@PathVariable("organization") organizationKey: String,
                 @PathVariable("id") id: Long, @PathVariable("athleteId") athleteId: Long): ModelAndView {
        val mav = ModelAndView("/sec/series")

        val series = seriesRepository.getOne(id)
        val athlete = athleteRepository.getOne(athleteId)

        val category = categoryRepository.findBySeriesIdAndGenderAndYearFromLessThanEqualAndYearToGreaterThanEqual(
                id, athlete.gender, athlete.yearOfBirth, athlete.yearOfBirth)

        if (category == null) {
            mav.model["athletes_message"] = "No matching category found for gender " + athlete.gender + " and year " + athlete.yearOfBirth
        } else {
            category.athletes.add(athlete)
            categoryRepository.save(category)
        }

        mav.model["series"] = series
        mav.model["categories"] = categoryRepository.findAllBySeriesId(id)
        mav.model["athletes"] = athleteRepository.findAthleteDTOsBySeriesId(id)

        mav.model["message"] = null
        return mav
    }

    @Transactional
    @GetMapping("sec/{organization}/series/{seriesId}/athlete/{athleteId}/delete")
    fun deleteById(@PathVariable("organization") organizationKey: String,
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


    @PostMapping("sec/{organization}/series")
    fun post(@AuthenticationPrincipal user: User,
             @PathVariable("organization") organizationKey: String,
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

    @GetMapping("/sec/{organization}/series/{id}/delete")
    fun deleteById(@AuthenticationPrincipal user: User,
                   @PathVariable("organization") organizationKey: String,
                   @PathVariable("id") id: Long): ModelAndView {
        seriesRepository.deleteById(id)

        val mav = ModelAndView("/sec/serieslist")

        val organization = organizationRepository.findByKey(organizationKey)
        mav.model["seriesList"] = seriesRepository.findByOrganizationId(organization.id!!)

        mav.model["message"] = null
        return mav
    }

}
