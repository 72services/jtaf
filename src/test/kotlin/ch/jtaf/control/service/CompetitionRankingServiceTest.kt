package ch.jtaf.control.service

import ch.jtaf.control.repository.CompetitionRepository
import ch.jtaf.control.repository.SeriesRepository
import ch.jtaf.entity.Competition
import ch.jtaf.entity.Series
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
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

    @Before
    fun createTestData() {
        val series = seriesRepository.save(Series(name = "Test"))
        seriesId = series.id!!

        val competition = competitionRepository.save(Competition(name = "Test", seriesId = seriesId))
        competitionId = competition.id!!
    }

    @After
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
