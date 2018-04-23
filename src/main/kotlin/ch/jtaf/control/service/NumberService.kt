package ch.jtaf.control.service

import ch.jtaf.control.reporting.report.Numbers
import ch.jtaf.control.repository.AthleteRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class NumberService(private val athleteRepository: AthleteRepository) {

    fun createNumbers(seriesId: Long, orderByClub: Boolean): ByteArray {
        val athletes = athleteRepository.findAthleteDTOsBySeriesId(seriesId)

        val numbers = Numbers(athletes, Locale.ENGLISH)

        return numbers.create()
    }

}
