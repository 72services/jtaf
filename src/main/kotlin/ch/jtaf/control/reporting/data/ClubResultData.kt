package ch.jtaf.control.reporting.data

import ch.jtaf.entity.Club

class ClubResultData(val club: Club, val points: Int) : Comparable<ClubResultData> {

    override fun compareTo(other: ClubResultData): Int {
        return other.points.compareTo(points)
    }

}
