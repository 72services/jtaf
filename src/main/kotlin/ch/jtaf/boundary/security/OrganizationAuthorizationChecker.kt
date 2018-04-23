package ch.jtaf.boundary.security

import ch.jtaf.control.repository.OrganizationRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class OrganizationAuthorizationChecker(private val organizationRepository: OrganizationRepository) {

    @Cacheable
    fun userHasAccessToOrganization(userName: String, organizationKey: String): Boolean {
        val organization = organizationRepository.findByKey(organizationKey)
        return organization.owner == userName
    }

}