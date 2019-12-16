package ch.jtaf.control.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@DataJpaTest
class CompetitionRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var competitionRepository: CompetitionRepository

    @Test
    fun findAll() {
        val list = competitionRepository.findAll()

        assertEquals(1, list.size)
        assertEquals("1. CIS Twann", list[0].name)
    }

}
