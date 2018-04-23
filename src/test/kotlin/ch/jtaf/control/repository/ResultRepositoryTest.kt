package ch.jtaf.control.repository

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class ResultRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var resultRepository: ResultRepository

    @Test
    fun findByCompetitionId() {
        val list = resultRepository.findByCompetitionId(competitionId)

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
