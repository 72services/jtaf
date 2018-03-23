package ch.jtaf.boundary.controller

import ch.jtaf.boundary.dto.Message
import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.entity.Organization
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class OrganizationController(private val organizationRepository: OrganizationRepository) {

    @GetMapping("/sec/organization")
    fun get(): ModelAndView {
        val mav = ModelAndView("sec/organization")
        mav.model["organization"] = Organization()

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/sec/organization/{organizationId}")
    fun getById(@PathVariable("organizationId") organizationId: Long): ModelAndView {
        val mav = ModelAndView("sec/organization")
        mav.model["organization"] = organizationRepository.getOne(organizationId)

        mav.model["message"] = null
        return mav
    }

    @PostMapping("/sec/organization")
    fun post(@AuthenticationPrincipal user: User,
             organization: Organization): ModelAndView {
        val mav = ModelAndView("sec/organization")

        if (organization.id == null) {
            organization.owner = user.username
            organizationRepository.save(organization)

            mav.model["organization"] = organization
        } else {
            val organizationFromDb = organizationRepository.getOne(organization.id!!)
            organizationFromDb.key = organization.key
            organizationFromDb.name = organization.name
            organizationRepository.save(organizationFromDb)

            mav.model["organization"] = organizationFromDb
        }

        mav.model["message"] = Message(Message.success, "Organization saved!")
        return mav
    }
}
