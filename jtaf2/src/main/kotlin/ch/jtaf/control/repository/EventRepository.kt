package ch.jtaf.control.repository

import ch.jtaf.entity.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long> {

    fun findByOrganizationId(organizationId: Long): List<Event>
}
