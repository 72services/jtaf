package ch.jtaf.boundary

import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.SeriesRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/dashboard")
class DashboardController(private val seriesRepository: SeriesRepository,
                          private val athleteRepository: AthleteRepository) {

    @GetMapping
    fun get(): ModelAndView {
        val mav = ModelAndView("/dashboard")

        val seriesList = seriesRepository.findAll()
        seriesList.forEach {
            val numberOfAthletes = athleteRepository.getTotalNumberOfAthletesForSeries(it.id!!)

            it.competitions.forEach {
                it.numberOfAthletes = numberOfAthletes ?: 0
                it.numberOfAthletesWithResults = athleteRepository.getTotalNumberOfAthleteWithResultsForCompetition(it.id!!) ?: 0
            }
        }

        mav.model["seriesList"] = seriesList

        return mav
    }
}
