package ch.jtaf.control.reporting.data

import ch.jtaf.entity.AthleteWithResultsDTO
import ch.jtaf.entity.Category

class SeriesRankingCategoryData(val category: Category, val athletes: List<AthleteWithResultsDTO>, private val numberOfCompetitions: Int) {

    fun getAthletesSortedByPointsDesc(): List<AthleteWithResultsDTO> {
        return athletes
                .filter { it.results.map { it.competition }.distinctBy { it?.id }.count() == numberOfCompetitions }
                .filter { it.results.sumBy { it.points } != 0 }
                .sortedByDescending { it.results.sumBy { it.points } }
    }
}