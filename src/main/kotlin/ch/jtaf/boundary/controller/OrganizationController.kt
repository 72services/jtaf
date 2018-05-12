package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.ORGANIZATION
import ch.jtaf.boundary.dto.Message
import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.entity.Organization
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class OrganizationController(private val organizationRepository: OrganizationRepository) {

    @GetMapping("/sec/organization")
    fun get(model: Model): String {
        model["organization"] = Organization()
        return ORGANIZATION
    }

    @GetMapping("/sec/organization/{organizationId}")
    fun getById(@PathVariable organizationId: Long, model: Model): String {
        model["organization"] = organizationRepository.getOne(organizationId)
        return ORGANIZATION
    }

    @PostMapping("/sec/organization")
    fun post(@AuthenticationPrincipal user: User, organization: Organization, model: Model): String {
        if (organization.id == null) {
            organization.owner = user.username
            organizationRepository.save(organization)

            model["organization"] = organization
        } else {
            val organizationFromDb = organizationRepository.getOne(organization.id!!)
            organizationFromDb.key = organization.key
            organizationFromDb.name = organization.name
            organizationRepository.save(organizationFromDb)

            model["organization"] = organizationFromDb
        }
        model["message"] = Message(Message.success, "Organization saved!")

        return ORGANIZATION
    }
}
