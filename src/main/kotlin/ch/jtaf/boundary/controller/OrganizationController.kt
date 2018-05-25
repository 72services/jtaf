package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.ORGANIZATION
import ch.jtaf.boundary.dto.Message
import ch.jtaf.boundary.security.CheckOrganizationAccess
import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.entity.Organization
import ch.jtaf.entity.OrganizationUser
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class OrganizationController(private val organizationRepository: OrganizationRepository) {

    @GetMapping("/sec/organization")
    fun get(@RequestParam organizationKey: String?, model: Model): String {
        model["organization"] = Organization()
        model["organizationKey"] = organizationKey ?: ""

        return ORGANIZATION
    }

    @GetMapping("/sec/organization/{organizationId}")
    fun getById(@PathVariable organizationId: Long, @RequestParam organizationKey: String?, model: Model): String {
        val organization = organizationRepository.getOne(organizationId)
        model["organization"] = organization
        model["organizationKey"] = organizationKey ?: ""
        return ORGANIZATION
    }

    @PostMapping("/sec/organization")
    fun post(@AuthenticationPrincipal user: User, @RequestParam organizationKey: String?,
             organization: Organization, model: Model): String {
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
        model["organizationKey"] = organizationKey ?: ""

        return ORGANIZATION
    }

    @GetMapping("/sec/organization/{organizationId}/share")
    fun getShares(@AuthenticationPrincipal user: User, @PathVariable organizationId: Long, @RequestParam organizationKey: String?, model: Model): String {
        val optional = organizationRepository.findById(organizationId)
        if (optional.isPresent) {
            val organization = optional.get()
            model["organizationUsers"] = organization.users
            model["organizationShare"] = OrganizationShare(organization)
        }
        model["organizationKey"] = organizationKey ?: ""

        return Views.SHARE
    }

}
