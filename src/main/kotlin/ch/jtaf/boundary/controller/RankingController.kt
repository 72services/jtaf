package ch.jtaf.boundary.controller

import ch.jtaf.control.service.CompetitionRankingService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class RankingController(private val rankingService: CompetitionRankingService) {

    @GetMapping("/ranking/competition/{id}")
    fun get(@PathVariable("id") competitionId: Long): ByteArray{
        return rankingService.createCompetitionRanking(competitionId)
    }

}
