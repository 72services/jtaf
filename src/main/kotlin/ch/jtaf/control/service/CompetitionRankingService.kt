package ch.jtaf.control.service

import ch.jtaf.control.reporting.data.CompetitionRankingCategoryData
import ch.jtaf.control.reporting.data.CompetitionRankingData
import ch.jtaf.control.reporting.report.CompetitionRanking
import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.CompetitionRepository
import ch.jtaf.control.repository.ResultRepository
import ch.jtaf.entity.AthleteWithResultsDTO
import ch.jtaf.entity.Competition
import org.springframework.stereotype.Component
import java.util.*

@Component
class CompetitionRankingService(private val competitionRepository: CompetitionRepository,
                                private val categoryRepository: CategoryRepository,
                                private val resultRepository: ResultRepository) {

    fun createCompetitionRanking(competitionId: Long): ByteArray {
        val competitionRanking = CompetitionRanking(getCompetitionRankingData(competitionId), Locale.ENGLISH)
        return competitionRanking.create()
    }

    fun getCompetitionRankingData(competitionId: Long): CompetitionRankingData {
        val competition = competitionRepository.getOne(competitionId)
        return CompetitionRankingData(competition, createCompetitionRankingCategoryData(competition))
    }

    private fun createCompetitionRankingCategoryData(competition: Competition): List<CompetitionRankingCategoryData> {
        val categories = categoryRepository.findAllBySeriesId(competition.seriesId!!)
        val results = resultRepository.findByCompetitionId(competition.id!!)

        return categories.map {
            CompetitionRankingCategoryData(it, it.athletes.map { athlete ->
                AthleteWithResultsDTO(athlete, results.filter { it.athlete == athlete })
            })
        }
    }

}
