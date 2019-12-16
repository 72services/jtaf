package ch.jtaf.control.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class ResultRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var resultRepository: ResultRepository

    @Test
    fun findByCompetitionId() {
        val list = resultRepository.findByCompetitionIdOrderByPosition(competitionId)

        assertEquals(0, list.size)
    }

    @Test
    fun findByCompetitionSeriesId() {
        val list = resultRepository.findByCompetitionSeriesId(seriesId)

        assertEquals(0, list.size)
    }

    @Test
    fun findByAthleteIdAndCompetitionIdOrderByPosition() {
        val list = resultRepository.findByAthleteIdAndCompetitionIdOrderByPosition(athleteId, competitionId)

        assertEquals(0, list.size)
    }

    @Test
    fun deleteResultsFromActiveCompetitions() {
        resultRepository.deleteResultsFromActiveCompetitions(competitionId)
    }

}
