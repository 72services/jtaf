package ch.jtaf.entity

data class AthleteWithResultsDTO(val athlete: Athlete, val results: List<Result>) {

    fun getTotalPoints(): Int = results.sumBy { it.points }

    fun getResultsAsString(): String {
        val sb = StringBuilder()
        results.forEach {
            sb.append(it.event?.abbreviation)
            sb.append(": ")
            sb.append(it.result)
            sb.append(" (")
            sb.append(it.points)
            sb.append(")")
            sb.append(" ")
        }
        return sb.toString()
    }
}