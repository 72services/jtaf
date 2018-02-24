package ch.jtaf.boundary

import ch.jtaf.control.repository.SeriesRepository
import ch.jtaf.entity.Series
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SeriesAuthorizationChecker(private val seriesRepository: SeriesRepository) {

    fun checkIfUserAccessToSeries(seriesId: Long?): Series? {
        val authentication = SecurityContextHolder.getContext().authentication

        if (seriesId == null) {
            throw IllegalArgumentException("seriesId may not be null")
        } else {
            val seriesOptional = seriesRepository.findById(seriesId)
            if (seriesOptional.isPresent) {
                val series = seriesOptional.get()
                if (series.owner == authentication.name) {
                    return series
                } else {
                    throw IllegalArgumentException("User is not owner of the series")
                }
            } else {
                throw IllegalArgumentException("Series does not exists")
            }
        }
    }
}