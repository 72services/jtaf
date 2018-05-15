package ch.jtaf.control.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
    fun findByOrganizationIdOrderByLastNameAscFirstNameAsc() {
        val list = athleteRepository.findByOrganizationIdOrderByLastNameAscFirstNameAsc(organizationId)

        assertEquals(1, list.size)
        assertEquals("Max", list[0].firstName)
    }

    @Test
    fun findByOrganizationIdAndNotAssignedToSeries() {
        val list = athleteRepository.findByOrganizationIdAndNotAssignedToSeries(organizationId, seriesId)

        assertEquals(0, list.size)
    }

    @Test
    fun findAthleteDTOsBySeriesIdOrderByCategory() {
        val dtos = athleteRepository.findAthleteDTOsBySeriesIdOrderByCategory(seriesId)

        assertEquals(1, dtos.size)
        assertEquals("Max", dtos.first().firstName)
    }

    @Test
    fun findAthleteDTOsBySeriesIdOrderByClub() {
        val dtos = athleteRepository.findAthleteDTOsBySeriesIdOrderByClub(seriesId)

        assertEquals(1, dtos.size)
        assertEquals("Max", dtos.first().firstName)
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

    @Test
    fun searchAthletes() {
        val athletes = athleteRepository.searchAthletes(seriesId, "Max%")

        assertEquals(1, athletes.size)
        assertEquals("Max", athletes.first().firstName)
    }

    @Test
    fun getOneAthleteDTO() {
        val athlete = athleteRepository.getOneAthleteDTO(athleteId)

        assertNotNull(athlete)
    }
}
