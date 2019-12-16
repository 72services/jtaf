package ch.jtaf.control.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@DataJpaTest
class EventRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var eventRepository: EventRepository

    @Test
    fun findAll() {
        val list = eventRepository.findByOrganizationIdOrderByAbbreviationAscGenderDesc(organizationId)

        assertEquals(1, list.size)
        assertEquals("80", list[0].abbreviation)
    }

}
