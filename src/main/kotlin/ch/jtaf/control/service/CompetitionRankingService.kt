package ch.jtaf.control.service

import ch.jtaf.control.reporting.data.CompetitionRankingCategoryData
import ch.jtaf.control.reporting.data.CompetitionRankingData
import ch.jtaf.control.reporting.data.EventsRankingData
import ch.jtaf.control.reporting.data.EventsRankingEventData
import ch.jtaf.control.reporting.report.CompetitionRanking
import ch.jtaf.control.reporting.report.Diplomas
import ch.jtaf.control.reporting.report.EventsRanking
import ch.jtaf.control.repository.*
import ch.jtaf.entity.AthleteWithResultsDTO
import ch.jtaf.entity.Competition
import ch.jtaf.entity.Result
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class CompetitionRankingService(private val competitionRepository: CompetitionRepository,
                                private val categoryRepository: CategoryRepository,
                                private val seriesRepository: SeriesRepository,
                                private val eventRepository: EventRepository,
                                private val resultRepository: ResultRepository) {

    fun createCompetitionRanking(competitionId: Long): ByteArray {
        val competitionRanking = CompetitionRanking(getCompetitionRankingData(competitionId), LocaleContextHolder.getLocale())
        return competitionRanking.create()
    }

    fun getCompetitionRankingData(competitionId: Long): CompetitionRankingData {
        val competition = competitionRepository.getOne(competitionId)
        return CompetitionRankingData(competition, createCompetitionRankingCategoryData(competition))
    }

    private fun createCompetitionRankingCategoryData(competition: Competition): List<CompetitionRankingCategoryData> {
        val categories = categoryRepository.findAllBySeriesIdOrderByAbbreviation(competition.seriesId!!)
        val results = resultRepository.findByCompetitionId(competition.id!!)

        return categories.map {
            CompetitionRankingCategoryData(it, it.athletes.map { athlete ->
                AthleteWithResultsDTO(athlete, results.filter { it.athlete == athlete })
            })
        }
    }

    fun createEventsRanking(competitionId: Long): ByteArray {
        val competition = competitionRepository.getOne(competitionId)
        val series = seriesRepository.getOne(competition.seriesId!!)
        val events = eventRepository.findByOrganizationId(series.organizationId!!)
        val results = resultRepository.findByCompetitionId(competition.id!!)

        val list = events.map { event ->
            EventsRankingEventData(event, results.filter { result -> event == result.event }.sortedBy { result -> result.toInt() })
        }
        return EventsRanking(EventsRankingData(competition, list), LocaleContextHolder.getLocale()).create()
    }

    fun createDiplomas(competitionId: Long): ByteArray {
        val competition = competitionRepository.getOne(competitionId)
        val series = seriesRepository.getOne(competition.seriesId!!)
        val competitionRankingData = getCompetitionRankingData(competitionId)

        return Diplomas(competitionRankingData, series.logo, LocaleContextHolder.getLocale()).create()
    }

    private fun resultStringToInt(): (Result) -> Int = { it.result.replace("\\.".toRegex(), "").toInt() }

}
