package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.ATHLETES
import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.OrganizationRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@Controller
class AthletesController(private val athleteRepository: AthleteRepository,
                         private val organizationRepository: OrganizationRepository) {

    @GetMapping("/sec/{organizationKey}/athletes")
    fun get(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String, @RequestParam mode: String?,
            @RequestParam seriesId: Long?, model: Model): String {

        val organization = organizationRepository.findByKey(organizationKey)

        model["athletes"] = if (mode == "select") {
            athleteRepository.findByOrganizationIdAndNotAssignedToSeries(organization.id!!, seriesId)
        } else {
            athleteRepository.findByOrganizationIdOrderByLastNameAscFirstNameAsc(organization.id!!)
        }

        model["mode"] = mode ?: "edit"
        if (seriesId != null) {
            model["seriesId"] = seriesId
        }

        return ATHLETES
    }

    @GetMapping("/sec/{organizationKey}/athletes/{athleteId}/delete")
    fun deleteById(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String, @PathVariable athleteId: Long,
                   model: Model): String {

        athleteRepository.deleteById(athleteId)

        val organization = organizationRepository.findByKey(organizationKey)
        model["athletes"] = athleteRepository.findByOrganizationIdOrderByLastNameAscFirstNameAsc(organization.id!!)

        model["mode"] = "edit"

        return ATHLETES
    }
}
