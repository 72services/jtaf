package ch.jtaf.boundary

import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.control.repository.SeriesRepository
import ch.jtaf.entity.Series
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class OrganizationAuthorizationChecker(private val organizationRepository: OrganizationRepository) {

    fun userHasAccessToOrganization(organization: String) {
        val authentication = SecurityContextHolder.getContext().authentication

        val organization = organizationRepository.findByKey(organization)
        if (organization.owner != authentication.name) {
            throw IllegalArgumentException("User is not granted to access this organization")
        }
    }

}