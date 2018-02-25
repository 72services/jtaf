package ch.jtaf.control.repository

import ch.jtaf.AbstractBaseDataTest
import ch.jtaf.entity.Gender
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class CompetitionRepositoryTest : AbstractBaseDataTest() {

    @Autowired
    lateinit var competitionRepository: CompetitionRepository

    @Test
    fun findAll() {
        val list = competitionRepository.findAll()

        assertEquals(1, list.size)
        assertEquals("1. CIS Twann", list[0].name)
    }

}
