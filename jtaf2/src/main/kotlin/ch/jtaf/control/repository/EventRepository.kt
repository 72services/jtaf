package ch.jtaf.control.repository

import ch.jtaf.entity.Category
import ch.jtaf.entity.Event
import ch.jtaf.entity.Series
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long> {

    fun findAllByOwner(username: String): List<Event>
}
