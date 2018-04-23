package ch.jtaf.boundary.controller

data class SearchRequest(var seriesId: Long, var competitionId: Long, var term: String = "")