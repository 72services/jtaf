package ch.jtaf.boundary.controller

import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.control.repository.SeriesRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.ModelAndView

@Controller
class SeriesListController(private val seriesRepository: SeriesRepository,
                           private val athleteRepository: AthleteRepository,
                           private val organizationRepository: OrganizationRepository) {

    @GetMapping("/sec/{organizationKey}")
    fun get(@AuthenticationPrincipal user: User,
            @PathVariable("organizationKey") organizationKey: String): ModelAndView {
        val organization = organizationRepository.findByKey(organizationKey)
        val seriesList = seriesRepository.findByOrganizationId(organization.id!!)

        seriesList.forEach {
            val numberOfAthletes = athleteRepository.getTotalNumberOfAthletesForSeries(it.id!!)

            it.competitions.forEach {
                it.numberOfAthletes = numberOfAthletes ?: 0
                it.numberOfAthletesWithResults = athleteRepository.getTotalNumberOfAthleteWithResultsForCompetition(it.id!!) ?: 0
            }
        }

        val mav = ModelAndView("sec/serieslist")
        mav.model["seriesList"] = seriesList
        return mav
    }

}
