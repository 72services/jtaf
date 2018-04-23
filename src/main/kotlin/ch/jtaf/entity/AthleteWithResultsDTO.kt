package ch.jtaf.entity

data class AthleteWithResultsDTO(val athlete: Athlete, val results: List<Result>) {

    fun getTotalPoints(): Int = results.sumBy { it.points }

    fun competitionResultsAsString(): String {
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

    fun seriesResultsAsString(): String {
        val sb = StringBuilder()
        getPointsPerCompetiton().forEach {
            sb.append(it.key.name)
            sb.append(": ")
            sb.append(it.value)
            sb.append(" ")
        }
        return sb.toString()
    }

    private fun getPointsPerCompetiton(): HashMap<Competition, Int> {
        var map = HashMap<Competition, Int>()
        results.sortedBy { it.competition!!.competitionDate }.forEach {
            if (map.containsKey(it.competition!!)) {
                map.put(it.competition!!, map.get(it.competition!!)!! + it.points)
            } else {
                map.put(it.competition!!, it.points)
            }
        }
        return map
    }

}