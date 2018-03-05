package ch.jtaf.boundary.controller

import ch.jtaf.entity.Result

data class ResultContainer(var seriesId: Long, var competitionId: Long, var athleteId: Long?, var results: List<Result> = ArrayList())