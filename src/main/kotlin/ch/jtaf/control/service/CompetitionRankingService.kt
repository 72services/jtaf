package ch.jtaf.control.service

import ch.jtaf.control.reporting.data.CompetitionRankingCategoryData
import ch.jtaf.control.reporting.data.CompetitionRankingData
import ch.jtaf.control.reporting.report.CompetitionRanking
import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.CompetitionRepository
import ch.jtaf.control.repository.ResultRepository
import ch.jtaf.entity.AthleteWithResultsDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class CompetitionRankingService(private val competitionRepository: CompetitionRepository,
                                private val categoryRepository: CategoryRepository,
                                private val resultRepository: ResultRepository) {

    fun createCompetitionRanking(competitionId: Long): ByteArray {
        val competition = competitionRepository.getOne(competitionId)
        val competitionRankingData = CompetitionRankingData(competition, createCompetitionRankingCategoryData(competitionId))

        val competitionRanking = CompetitionRanking(competitionRankingData, Locale.ENGLISH)

        return competitionRanking.create()
    }

    private fun createCompetitionRankingCategoryData(competitionId: Long): List<CompetitionRankingCategoryData> {
        val competition = competitionRepository.getOne(competitionId)
        val categories = categoryRepository.findAllBySeriesId(competition.seriesId!!)
        val results = resultRepository.findByCompetitionId(competitionId)

        return categories.map { category ->
            CompetitionRankingCategoryData(category, category.athletes
                    .map { athlete ->
                        AthleteWithResultsDTO(athlete, results
                                .filter { result ->
                                    result.athlete == athlete
                                }
                        )
                    }
            )
        }
    }

}
