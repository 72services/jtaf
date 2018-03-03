package ch.jtaf.boundary

import ch.jtaf.control.repository.OrganizationRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class OrganizationAuthorizationChecker(private val organizationRepository: OrganizationRepository) {

    fun userHasAccessToOrganization(organizationKey: String) {
        val authentication = SecurityContextHolder.getContext().authentication

        val organization = organizationRepository.findByKey(organizationKey)
        if (organization.owner != authentication.name) {
            throw IllegalArgumentException("User is not granted to access this organization")
        }
    }

}