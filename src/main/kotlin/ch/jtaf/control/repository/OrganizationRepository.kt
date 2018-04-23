package ch.jtaf.control.repository

import ch.jtaf.entity.Organization
import org.springframework.data.jpa.repository.JpaRepository

interface OrganizationRepository : JpaRepository<Organization, Long> {

    fun findByKey(key: String): Organization

    fun findAllByOwner(username: String?): List<Organization>
}
