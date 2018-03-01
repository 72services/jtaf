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
    fun findByOrganizationId() {
        val list = athleteRepository.findByOrganizationId(1)

        assertEquals(1, list.size)
        assertEquals("Max", list[0].firstName)
    }

    @Test
    fun findByOrganizationIdAndNotAssignedToSeries() {
        val list = athleteRepository.findByOrganizationIdAndNotAssignedToSeries(1, 1)

        assertEquals(0, list.size)
    }

    @Test
    fun findAthleteBySeriesId() {
        val dtos = athleteRepository.findAthleteBySeriesId(seriesId);

        assertEquals(1, dtos.size)
        assertEquals("Max", dtos[0].firstName)
    }

    @Test
    fun getTotalNumberOfAthletesForSeries() {
        val number = athleteRepository.getTotalNumberOfAthletesForSeries(seriesId)

        assertEquals(1, number)
    }

    @Test
    fun getTotalNumberOfAthleteWithResultsForCompetition() {
        val number = athleteRepository.getTotalNumberOfAthleteWithResultsForCompetition(seriesId) ?: 0

        assertEquals(0, number)
    }
}
