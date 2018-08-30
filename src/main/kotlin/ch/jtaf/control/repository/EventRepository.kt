package ch.jtaf.control.repository

import ch.jtaf.entity.Event
import ch.jtaf.entity.Gender
import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository : JpaRepository<Event, Long> {

    fun findByOrganizationIdOrderByAbbreviationAscGenderDesc(organizationId: Long): List<Event>

    fun findByOrganizationIdAndGenderOrderByAbbreviationAsc(organizationId: Long, gender: Gender): List<Event>
}
