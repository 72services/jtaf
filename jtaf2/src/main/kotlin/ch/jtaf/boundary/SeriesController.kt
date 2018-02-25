package ch.jtaf.boundary

import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.SeriesRepository
import ch.jtaf.entity.Series
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/sec/series")
class SeriesController(private val seriesRepository: SeriesRepository,
                       private val categoryRepository: CategoryRepository,
                       private val athleteRepository: AthleteRepository,
                       private val seriesAuthorizationChecker: SeriesAuthorizationChecker) {

    @GetMapping()
    fun get(): ModelAndView {
        val mav = ModelAndView()
        mav.model["message"] = ""
        mav.model["series"] = Series()
        return mav
    }

    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: Long): ModelAndView {
        val series = seriesAuthorizationChecker.checkIfUserAccessToSeries(id)

        val mav = ModelAndView("/sec/series")
        mav.model["message"] = ""
        mav.model["series"] = series
        mav.model["categories"] = categoryRepository.findAllBySeriesId(id)
        mav.model["athletes"] = athleteRepository.findAthleteDTOsBySeriesId(id)

        return mav
    }

    @GetMapping("{id}/athlete/{athleteId}")
    fun addEvent(@PathVariable("id") id: Long, @PathVariable("athleteId") athleteId: Long): ModelAndView {
        val mav = ModelAndView("/sec/series")
        mav.model["message"] = ""

        val series = seriesRepository.getOne(id)
        val athlete = athleteRepository.getOne(athleteId)

        val category = categoryRepository.findByGenderAndYearFromLessThanEqualAndYearToGreaterThanEqual(
                athlete.gender, athlete.yearOfBirth, athlete.yearOfBirth)

        if (category == null) {
            mav.model["athletes_message"] = "No matching category found for gender " + athlete.gender + " and year " + athlete.yearOfBirth
        } else {
            category.athletes.add(athlete)
            categoryRepository.save(category)
        }

        mav.model["series"] = series
        mav.model["categories"] = categoryRepository.findAllBySeriesId(id)
        mav.model["athletes"] = athleteRepository.findAthleteDTOsBySeriesId(id)

        return mav
    }

    @GetMapping("{id}/event/{eventId}/delete")
    fun deleteById(@PathVariable("id") id: Long, @PathVariable("athleteId") athleteId: Long): ModelAndView {
        val mav = ModelAndView("/sec/series")
        mav.model["message"] = ""

        val series = seriesRepository.getOne(id)
        val athlete = athleteRepository.getOne(athleteId)

        val category = categoryRepository.getOne(id)
        category.athletes.remove(athlete)

        categoryRepository.save(category)

        mav.model["series"] = series
        mav.model["categories"] = categoryRepository.findAllBySeriesId(id)
        mav.model["athletes"] = athleteRepository.findAthleteDTOsBySeriesId(id)

        return mav
    }


    @PostMapping
    fun post(@AuthenticationPrincipal user: User, series: Series): ModelAndView {
        val mav = ModelAndView()

        if (series.id != null) {
            seriesAuthorizationChecker.checkIfUserAccessToSeries(series.id)
            val seriesFromDb = seriesRepository.getOne(series.id!!)
            seriesFromDb.name = series.name
            seriesFromDb.locked = series.locked
            seriesFromDb.hidden = series.hidden
            seriesFromDb.owner = series.owner
            seriesRepository.save(seriesFromDb)

            mav.model["series"] = seriesFromDb
        }
        else {
            series.owner = user.username
            mav.model["series"] = series
        }


        mav.model["message"] = "Series saved!"

        mav.model["categories"] = categoryRepository.findAllBySeriesId(series.id!!)
        mav.model["athletes"] = athleteRepository.findAthleteDTOsBySeriesId(series.id!!)

        return mav
    }
}
