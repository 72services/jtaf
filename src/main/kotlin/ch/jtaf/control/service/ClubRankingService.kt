package ch.jtaf.control.service

import ch.jtaf.control.reporting.data.ClubRankingData
import ch.jtaf.control.reporting.data.ClubResultData
import ch.jtaf.control.reporting.report.ClubRanking
import ch.jtaf.control.repository.SeriesRepository
import ch.jtaf.entity.Club
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class ClubRankingService(private val seriesRepository: SeriesRepository,
                         private val seriesRankingService: SeriesRankingService) {

    fun createClubRanking(seriesId: Long): ByteArray {
        val clubRankingData = getClubRankingData(seriesId)
        val clubRanking = ClubRanking(clubRankingData,  LocaleContextHolder.getLocale())

        return clubRanking.create()
    }

    private fun getClubRankingData(seriesId: Long): ClubRankingData {
        val series = seriesRepository.findById(seriesId).get()
        return ClubRankingData(series, getClubResultData(seriesId))
    }

    private fun getClubResultData(seriesId: Long): List<ClubResultData> {
        val seriesRankingData = seriesRankingService.getSeriesRankingData(seriesId)
        val pointsPerClub = HashMap<Club, ClubResultData>()
        seriesRankingData.categories.forEach {
            var points = it.getAthletesSortedByPointsDesc().size
            it.getAthletesSortedByPointsDesc().forEach {
                if (pointsPerClub.containsKey(it.athlete.club)) {
                    val clubResultData = pointsPerClub[it.athlete.club]
                    clubResultData?.points = clubResultData?.points!! + it.getTotalPoints()
                } else {
                    pointsPerClub[it.athlete.club!!] = ClubResultData(it.athlete.club!!, points)
                }
            }
            --points
        }
        return pointsPerClub.values.toList()
    }

}
