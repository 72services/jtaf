package ch.jtaf.control.repository

import ch.jtaf.AbstractBaseDataTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class AthleteRepositoryTest : AbstractBaseDataTest() {

    @Autowired
    lateinit var athleteRepository: AthleteRepository

    @Test
    fun findAthleteDTOsBySeriesId() {
        val dtos = athleteRepository.findAthleteDTOsBySeriesId(1);

        assertFalse(dtos.isEmpty())
        assertEquals(1, dtos.size)
        assertEquals("Max", dtos[0].firstName)
    }

}
