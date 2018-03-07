package ch.jtaf.control.reporting.data

import ch.jtaf.entity.AthleteWithResultsDTO
import ch.jtaf.entity.Category
import java.util.*

class CompetitionRankingCategoryData(val category: Category, val athletes: List<AthleteWithResultsDTO>) : Comparable<CompetitionRankingCategoryData> {

    override fun hashCode(): Int {
        var hash = 7
        hash = 73 * hash + Objects.hashCode(this.category)
        return hash
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        val otherCategory = other as CompetitionRankingCategoryData?
        return this.category == otherCategory!!.category
    }

    override fun compareTo(other: CompetitionRankingCategoryData): Int {
        return this.category.abbreviation.compareTo(other.category.abbreviation)
    }
}
