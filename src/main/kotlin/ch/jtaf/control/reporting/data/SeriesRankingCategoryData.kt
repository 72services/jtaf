package ch.jtaf.control.reporting.data

import ch.jtaf.entity.AthleteWithResultsDTO
import ch.jtaf.entity.Category
import java.util.*

class SeriesRankingCategoryData(val category: Category, val athletes: List<AthleteWithResultsDTO>) : Comparable<SeriesRankingCategoryData> {

    override fun hashCode(): Int {
        var hash = 7
        hash = 89 * hash + Objects.hashCode(this.category)
        return hash
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        val otherCategory = other as SeriesRankingCategoryData?
        return this.category == otherCategory!!.category
    }

    override fun compareTo(other: SeriesRankingCategoryData): Int {
        return this.category.abbreviation.compareTo(other.category.abbreviation)
    }
}
