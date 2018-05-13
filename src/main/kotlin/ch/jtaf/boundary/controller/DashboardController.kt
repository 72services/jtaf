package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.DASHBOARD
import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.SeriesRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/dashboard")
class DashboardController(private val seriesRepository: SeriesRepository,
                          private val athleteRepository: AthleteRepository) {

    @GetMapping("{organizationKey}")
    fun get(@PathVariable organizationKey: String?, model: Model): String {
        val seriesList = seriesRepository.findAll()
        seriesList.forEach {
            val numberOfAthletes = athleteRepository.getTotalNumberOfAthletesForSeries(it.id!!)

            it.competitions.forEach {
                it.numberOfAthletes = numberOfAthletes ?: 0
                it.numberOfAthletesWithResults = athleteRepository.getTotalNumberOfAthleteWithResultsForCompetition(it.id!!) ?: 0
            }
        }

        model["seriesList"] = seriesList
        if (organizationKey != null) {
            model["organizationKey"] = organizationKey
        }

        return DASHBOARD
    }

    @GetMapping
    fun get(model: Model): String {
        return get(null, model)
    }
}
