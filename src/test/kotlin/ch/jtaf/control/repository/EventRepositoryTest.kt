package ch.jtaf.control.repository

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class EventRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var eventRepository: EventRepository

    @Test
    fun findAll() {
        val list = eventRepository.findByOrganizationId(organizationId)

        assertEquals(1, list.size)
        assertEquals("80", list[0].abbreviation)
    }

}
