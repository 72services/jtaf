package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.CLUB
import ch.jtaf.boundary.dto.Message
import ch.jtaf.boundary.security.CheckOrganizationAccess
import ch.jtaf.control.repository.ClubRepository
import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.entity.Club
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class ClubController(private val clubRepository: ClubRepository,
                     private val organizationRepository: OrganizationRepository) {

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/club")
    fun get(@PathVariable organizationKey: String, model: Model): String {
        val club = Club()
        model["club"] = club

        return CLUB
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/club/{clubId}")
    fun getById(@PathVariable organizationKey: String, @PathVariable clubId: Long, model: Model): String {
        model["club"] = clubRepository.getOne(clubId)

        return CLUB
    }

    @CheckOrganizationAccess
    @PostMapping("/sec/{organizationKey}/club")
    fun post(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String, club: Club, model: Model): String {
        val organization = organizationRepository.findByKey(organizationKey)
        club.organizationId = organization.id

        clubRepository.save(club)

        model["message"] = Message(Message.success, "Club saved!")

        return CLUB
    }
}
