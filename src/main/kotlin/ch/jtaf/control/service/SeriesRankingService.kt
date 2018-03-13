package ch.jtaf.control.service

import ch.jtaf.control.reporting.data.CompetitionRankingCategoryData
import ch.jtaf.control.reporting.data.SeriesRankingCategoryData
import ch.jtaf.control.reporting.data.SeriesRankingData
import ch.jtaf.control.reporting.report.SeriesRanking
import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.ResultRepository
import ch.jtaf.control.repository.SeriesRepository
import ch.jtaf.entity.AthleteWithResultsDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class SeriesRankingService(private val seriesRepository: SeriesRepository,
                           private val categoryRepository: CategoryRepository,
                           private val resultRepository: ResultRepository) {

    fun createSeriesRanking(seriesId: Long): ByteArray {
        val seriesRankingData = createSeriesRankingData(seriesId)
        val seriesRanking = SeriesRanking(seriesRankingData, Locale.ENGLISH)
        return seriesRanking.create()
    }

    fun getSeriesRankingData(seriesId: Long): SeriesRankingData {
        return createSeriesRankingData(seriesId)
    }

    private fun createSeriesRankingData(seriesId: Long): SeriesRankingData {
        val series = seriesRepository.getOne(seriesId)

        val categories = categoryRepository.findAllBySeriesId(seriesId)
        val results = resultRepository.findByCompetitionSeriesId(seriesId)

        return SeriesRankingData(series, categories.map {
            SeriesRankingCategoryData(it, it.athletes.map { athlete ->
                AthleteWithResultsDTO(athlete, results.filter { it.athlete == athlete })
            })
        })
    }
}