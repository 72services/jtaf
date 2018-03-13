package ch.jtaf.boundary.controller

import ch.jtaf.control.service.CompetitionRankingService
import ch.jtaf.control.service.SeriesRankingService
import org.springframework.http.MediaType.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.ModelAndView


@Controller
class RankingController(private val competitionRankingService: CompetitionRankingService,
                        private val seriesRankingService: SeriesRankingService) {

    val httpContentUtil = HttpContentUtil()

    @GetMapping("/ranking/competition/{id}")
    fun getCompetitionRanking(@PathVariable("id") competitionId: Long): ModelAndView {
        val mav = ModelAndView("/competition_ranking")
        mav.model["data"] = competitionRankingService.getCompetitionRankingData(competitionId)
        return mav
    }

    @GetMapping("/ranking/competition/{id}/pdf", produces = [APPLICATION_PDF_VALUE])
    fun getCompetitionRankingAsPdf(@PathVariable("id") competitionId: Long): ResponseEntity<ByteArray> {
        val competitionRanking = competitionRankingService.createCompetitionRanking(competitionId)
        return httpContentUtil.getContentAsPdf("competition_$competitionId.pdf", competitionRanking)
    }

    @GetMapping("/ranking/series/{id}")
    fun getSeriesRanking(@PathVariable("id") seriesId: Long): ModelAndView {
        val mav = ModelAndView("/series_ranking")
        mav.model["data"] = seriesRankingService.getSeriesRankingData(seriesId)
        return mav
    }

    @GetMapping("/ranking/series/{id}/pdf", produces = [APPLICATION_PDF_VALUE])
    fun getSeriesRankingAsPdf(@PathVariable("id") seriesId: Long): ResponseEntity<ByteArray> {
        val competitionRanking = seriesRankingService.createSeriesRanking(seriesId)
        return httpContentUtil.getContentAsPdf("series_$seriesId.pdf", competitionRanking)
    }

}
