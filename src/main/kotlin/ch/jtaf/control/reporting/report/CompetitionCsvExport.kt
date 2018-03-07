package ch.jtaf.control.reporting.report

import ch.jtaf.control.reporting.data.CompetitionRankingData

class CompetitionCsvExport(private val ranking: CompetitionRankingData) {

    fun create(): String {
        val sb = StringBuilder()
        for (category in ranking.categories) {
            var position = 1
            for (athlete in category.athletes) {
                sb.append(position)
                sb.append(",")
                sb.append(athlete.athlete.lastName)
                sb.append(",")
                sb.append(athlete.athlete.firstName)
                sb.append(",")
                sb.append(category.category.abbreviation)
                sb.append("\n")
                position++
            }
        }
        return sb.toString()
    }

}
