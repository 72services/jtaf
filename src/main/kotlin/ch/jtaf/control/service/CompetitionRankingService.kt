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
import org.springframework.stereotype.Component
import java.util.*

@Component
class CompetitionRankingService(private val competitionRepository: CompetitionRepository,
                                private val categoryRepository: CategoryRepository,
                                private val seriesRepository: SeriesRepository,
                                private val eventRepository: EventRepository,
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
            EventsRankingEventData(event, results.filter { event == it.event }.sortedBy { it.result.replace("\\.".toRegex(), "").toInt() })
        }
        return EventsRanking(EventsRankingData(competition, list), Locale.ENGLISH).create()
    }

    fun createDiplomas(competitionId: Long): ByteArray {
        val competition = competitionRepository.getOne(competitionId)
        val series = seriesRepository.getOne(competition.seriesId!!)
        val competitionRankingData = getCompetitionRankingData(competitionId)

        return Diplomas(competitionRankingData, series.logo, Locale.ENGLISH).create()
    }

}
