package ch.jtaf.boundary

import ch.jtaf.control.repository.ClubRepository
import ch.jtaf.control.repository.OrganizationRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.ModelAndView

@Controller
class ClubsController(private val clubRepository: ClubRepository,
                      private val organizationRepository: OrganizationRepository) {

    @GetMapping("/sec/{organization}/clubs")
    fun get(@AuthenticationPrincipal user: User,
            @PathVariable("organization") organizationKey: String): ModelAndView {
        val mav = ModelAndView("/sec/clubs")

        val organization = organizationRepository.findByKey(organizationKey)
        mav.model["clubs"] = clubRepository.findByOrganizationId(organization.id!!)

        return mav
    }
}
