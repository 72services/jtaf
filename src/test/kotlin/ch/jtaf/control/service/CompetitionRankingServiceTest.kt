package ch.jtaf.control.service

import ch.jtaf.control.repository.CompetitionRepository
import ch.jtaf.control.repository.SeriesRepository
import ch.jtaf.entity.Competition
import ch.jtaf.entity.Series
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CompetitionRankingServiceTest {

    @Autowired
    lateinit var competitionRankingService: CompetitionRankingService
    @Autowired
    lateinit var competitionRepository: CompetitionRepository;
    @Autowired
    lateinit var seriesRepository: SeriesRepository

    private var competitionId: Long = 0L
    private var seriesId: Long = 0L

    @BeforeEach
    fun createTestData() {
        val series = seriesRepository.save(Series(name = "Test"))
        seriesId = series.id!!

        val competition = competitionRepository.save(Competition(name = "Test", seriesId = seriesId))
        competitionId = competition.id!!
    }

    @AfterEach
    fun removeTestData() {
        competitionRepository.deleteById(competitionId)
        seriesRepository.deleteById(seriesId)
    }

    @Test
    fun createCompetitionRanking() {
        val competitionRanking = competitionRankingService.createCompetitionRanking(competitionId)

        assertNotNull(competitionRanking)
    }

}
