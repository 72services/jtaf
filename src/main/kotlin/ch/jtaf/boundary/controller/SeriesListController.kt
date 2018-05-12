package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.SERIESLIST
import ch.jtaf.boundary.security.CheckOrganizationAccess
import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.control.repository.SeriesRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class SeriesListController(private val seriesRepository: SeriesRepository,
                           private val athleteRepository: AthleteRepository,
                           private val organizationRepository: OrganizationRepository) {

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}")
    fun get(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String, model: Model): String {
        val organization = organizationRepository.findByKey(organizationKey)
        val seriesList = seriesRepository.findByOrganizationId(organization.id!!)

        seriesList.forEach {
            val numberOfAthletes = athleteRepository.getTotalNumberOfAthletesForSeries(it.id!!)

            it.competitions.forEach {
                it.numberOfAthletes = numberOfAthletes ?: 0
                it.numberOfAthletesWithResults = athleteRepository.getTotalNumberOfAthleteWithResultsForCompetition(it.id!!) ?: 0
            }
        }
        model["seriesList"] = seriesList

        return SERIESLIST
    }

}
