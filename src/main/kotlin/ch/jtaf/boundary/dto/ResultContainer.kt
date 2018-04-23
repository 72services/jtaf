package ch.jtaf.boundary.dto

import ch.jtaf.entity.Result

data class ResultContainer(var seriesId: Long, var competitionId: Long, var athleteId: Long?, var results: List<Result> = ArrayList())