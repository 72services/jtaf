package ch.jtaf.control.repository

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class AthleteRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var athleteRepository: AthleteRepository

    @Test
    fun findAthleteDTOsBySeriesId() {
        val dtos = athleteRepository.findAthleteDTOsBySeriesId(1);

        assertEquals(1, dtos.size)
        assertEquals("Max", dtos[0].firstName)
    }

}
