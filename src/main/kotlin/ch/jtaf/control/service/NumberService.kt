package ch.jtaf.control.service

import ch.jtaf.control.reporting.report.Numbers
import ch.jtaf.control.repository.AthleteRepository
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class NumberService(private val athleteRepository: AthleteRepository) {

    fun createNumbers(seriesId: Long, orderByClub: Boolean): ByteArray {
        val athletes = if (orderByClub) {
            athleteRepository.findAthleteDTOsBySeriesIdOrderByClub(seriesId)
        } else {
            athleteRepository.findAthleteDTOsBySeriesIdOrderByCategory(seriesId)
        }

        val numbers = Numbers(athletes, LocaleContextHolder.getLocale())

        return numbers.create()
    }

}
