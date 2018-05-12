package ch.jtaf.control.service

import ch.jtaf.control.reporting.data.ClubRankingData
import ch.jtaf.control.reporting.data.ClubResultData
import ch.jtaf.control.reporting.report.ClubRanking
import ch.jtaf.control.repository.SeriesRepository
import ch.jtaf.entity.Club
import org.springframework.stereotype.Component
import java.util.*

@Component
class ClubRankingService(private val seriesRepository: SeriesRepository,
                         private val seriesRankingService: SeriesRankingService) {

    fun createClubRanking(seriesId: Long): ByteArray {
        val clubRankingData = getClubRankingData(seriesId)
        val clubRanking = ClubRanking(clubRankingData, Locale.ENGLISH)

        return clubRanking.create()
    }

    private fun getClubRankingData(seriesId: Long): ClubRankingData {
        val series = seriesRepository.findById(seriesId).get()
        return ClubRankingData(series, getClubResultData(seriesId))
    }

    private fun getClubResultData(seriesId: Long): List<ClubResultData> {
        val seriesRankingData = seriesRankingService.getSeriesRankingData(seriesId)
        val pointsPerClub = HashMap<Club, ClubResultData>()
        for (categoryData in seriesRankingData.categories) {
            var points = categoryData.athletes.size
            for (athlete in categoryData.athletes) {
                if (pointsPerClub.containsKey(athlete.athlete.club)) {
                    val clubResultData = pointsPerClub.get(athlete.athlete.club)
                    clubResultData?.points = clubResultData?.points!! + athlete.getTotalPoints()
                } else {
                    pointsPerClub.put(athlete.athlete.club!!, ClubResultData(athlete.athlete.club!!, points))
                }
            }
            --points
        }
        return pointsPerClub.values.toList()
    }

}
