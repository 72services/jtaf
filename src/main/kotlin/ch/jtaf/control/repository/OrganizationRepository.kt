package ch.jtaf.control.repository

import ch.jtaf.entity.Organization
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizationRepository : JpaRepository<Organization, Long> {

    fun findByKey(key: String): Organization

    fun findAllByOwner(username: String?): List<Organization>
}
