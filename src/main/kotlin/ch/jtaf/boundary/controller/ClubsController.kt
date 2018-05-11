package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.CLUBS
import ch.jtaf.control.repository.ClubRepository
import ch.jtaf.control.repository.OrganizationRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ClubsController(private val clubRepository: ClubRepository,
                      private val organizationRepository: OrganizationRepository) {

    @GetMapping("/sec/{organizationKey}/clubs")
    fun get(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String, model: Model): String {
        val organization = organizationRepository.findByKey(organizationKey)
        model["clubs"] = clubRepository.findByOrganizationIdOrderByAbbreviation(organization.id!!)

        return CLUBS
    }
}
