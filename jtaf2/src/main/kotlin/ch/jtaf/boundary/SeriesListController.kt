package ch.jtaf.boundary

import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.SeriesRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.util.function.Consumer

@Controller
@RequestMapping("/sec/serieslist")
class SeriesListController(private val seriesRepository: SeriesRepository,
                           private val athleteRepository: AthleteRepository,
                           private val seriesAuthorizationChecker: SeriesAuthorizationChecker) {

    @GetMapping
    fun get(@AuthenticationPrincipal user: User): ModelAndView {
        val mav = ModelAndView()
        fillSeriesData(mav, user)
        return mav
    }

    @GetMapping("{id}/delete")
    fun deleteById(@AuthenticationPrincipal user: User, @PathVariable("id") id: Long): ModelAndView {
        seriesAuthorizationChecker.checkIfUserAccessToSeries(id)

        seriesRepository.deleteById(id)

        val mav = ModelAndView("/sec/serieslist")
        fillSeriesData(mav, user)
        return mav
    }

    private fun fillSeriesData(mav: ModelAndView, user: User) {
        val seriesList = seriesRepository.findAllByOwner(user.username)
        seriesList.forEach {
            val numberOfAthletes = athleteRepository.getTotalNumberOfAthletesForSeries(it.id!!)

            it.competitions.forEach {
                it.numberOfAthletes = numberOfAthletes ?: 0
                it.numberOfAthletesWithResults = athleteRepository.getTotalNumberOfAthleteWithResultsForCompetition(it.id!!) ?: 0
            }
        }

        mav.model["seriesList"] = seriesList
    }

}
