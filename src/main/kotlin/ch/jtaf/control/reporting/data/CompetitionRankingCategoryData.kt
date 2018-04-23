package ch.jtaf.control.reporting.data

import ch.jtaf.entity.AthleteWithResultsDTO
import ch.jtaf.entity.Category

class CompetitionRankingCategoryData(val category: Category, val athletes: List<AthleteWithResultsDTO>) {

    fun getAthletesSortedByPointsDesc(): List<AthleteWithResultsDTO> {
        return athletes.filter { it.results.sumBy { it.points } != 0 }.sortedByDescending { it.results.sumBy { it.points } }
    }
}