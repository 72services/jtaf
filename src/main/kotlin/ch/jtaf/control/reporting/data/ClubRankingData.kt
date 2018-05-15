package ch.jtaf.control.reporting.data

import ch.jtaf.entity.Series

class ClubRankingData(val series: Series, val clubs: List<ClubResultData>) {

    fun getSortedClubs(): List<ClubResultData> {
        return clubs.sortedByDescending { it.points }
    }
}
