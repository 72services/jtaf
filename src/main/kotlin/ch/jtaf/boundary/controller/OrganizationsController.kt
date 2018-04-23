package ch.jtaf.boundary.controller

import ch.jtaf.control.repository.OrganizationRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class OrganizationsController(private val organizationRepository: OrganizationRepository) {

    @GetMapping("/sec/organizations")
    fun get(@AuthenticationPrincipal user: User): ModelAndView {
        val mav = ModelAndView("sec/organizations")
        mav.model["organizations"] = organizationRepository.findAllByOwner(user.username)
        return mav
    }

}
