package ch.jtaf.control.service

import ch.jtaf.control.reporting.report.Sheets
import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.CompetitionRepository
import ch.jtaf.control.repository.SeriesRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class SheetService(private val competitionRepository: CompetitionRepository,
                   private val seriesRepository: SeriesRepository,
                   private val categoryRepository: CategoryRepository,
                   private val athleteRepository: AthleteRepository) {

    fun createSheets(competitionId: Long, orderByClub: Boolean): ByteArray {
        val competition = competitionRepository.getOne(competitionId)
        val series = seriesRepository.getOne(competition.seriesId!!)
        val athletes = athleteRepository.findAthleteDTOsBySeriesId(competition.seriesId!!)
        val categories = categoryRepository.findAllBySeriesId(competition.seriesId!!)

        val sheets = Sheets(competition, athletes, categories, series.logo, Locale.ENGLISH)

        return sheets.create()
    }

}