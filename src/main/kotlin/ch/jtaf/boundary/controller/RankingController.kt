package ch.jtaf.boundary.controller

import ch.jtaf.control.service.CompetitionRankingService
import org.springframework.http.MediaType
import org.springframework.http.MediaType.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.http.HttpStatus
import org.springframework.http.HttpHeaders
import org.springframework.web.servlet.ModelAndView


@Controller
class RankingController(private val rankingService: CompetitionRankingService) {

    val httpContentUtil = HttpContentUtil()

    @GetMapping("/ranking/competition/{id}/pdf", produces = [APPLICATION_PDF_VALUE])
    fun getCompetitionRankingAsPdf(@PathVariable("id") competitionId: Long): ResponseEntity<ByteArray> {
        val competitionRanking = rankingService.createCompetitionRanking(competitionId)
        return httpContentUtil.getContentAsPdf("competition_$competitionId.pdf", competitionRanking)
    }

    @GetMapping("/ranking/competition/{id}")
    fun getCompetitionRanking(@PathVariable("id") competitionId: Long): ModelAndView {
        val mav = ModelAndView("/ranking/competition")
        mav.model["competitionRanking"] = rankingService.getCompetitionRankingData(competitionId)
        return mav
    }

}
