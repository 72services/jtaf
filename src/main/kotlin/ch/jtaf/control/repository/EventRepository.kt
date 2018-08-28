package ch.jtaf.control.repository

import ch.jtaf.entity.Event
import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository : JpaRepository<Event, Long> {

    fun findByOrganizationIdOrderByAbbreviationAscGenderDesc(organizationId: Long): List<Event>
}
