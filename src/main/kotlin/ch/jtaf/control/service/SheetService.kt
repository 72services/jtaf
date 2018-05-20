package ch.jtaf.control.service

import ch.jtaf.control.reporting.report.Sheets
import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.CompetitionRepository
import ch.jtaf.control.repository.SeriesRepository
import ch.jtaf.entity.AthleteDTO
import org.springframework.context.i18n.LocaleContextHolder
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

        var athletes =
                if (orderByClub) {
                    athleteRepository.findAthleteDTOsBySeriesIdOrderByClub(competition.seriesId!!)
                } else {
                    athleteRepository.findAthleteDTOsBySeriesIdOrderByCategory(competition.seriesId!!)
                }

        val categories = categoryRepository.findAllBySeriesIdOrderByAbbreviation(competition.seriesId!!)

        val sheets = Sheets(competition, athletes, categories, series.logo, LocaleContextHolder.getLocale())

        return sheets.create()
    }

    fun createEmptySheet(seriesId: Long, categoryid: Long): ByteArray {
        val category = categoryRepository.getOne(categoryid)
        val series = seriesRepository.getOne(seriesId)

        val sheet = Sheets(AthleteDTO(category = category.abbreviation), Arrays.asList(category), series.logo, LocaleContextHolder.getLocale())
        return sheet.create()
    }

}
