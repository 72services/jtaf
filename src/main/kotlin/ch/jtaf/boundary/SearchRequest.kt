package ch.jtaf.boundary

data class SearchRequest(var seriesId: Long, var competitionId: Long, var term: String = "")