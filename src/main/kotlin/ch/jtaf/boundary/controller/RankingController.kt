package ch.jtaf.boundary.controller

import ch.jtaf.boundary.web.HttpContentProducer
import ch.jtaf.control.service.ClubRankingService
import ch.jtaf.control.service.CompetitionRankingService
import ch.jtaf.control.service.SeriesRankingService
import org.springframework.http.MediaType.APPLICATION_PDF_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.ModelAndView


@Controller
class RankingController(private val competitionRankingService: CompetitionRankingService,
                        private val seriesRankingService: SeriesRankingService,
                        private val clubRankingService: ClubRankingService) {

    val httpContentUtil = HttpContentProducer()

    @GetMapping("/ranking/competition/{competitionId}")
    fun getCompetitionRanking(@PathVariable("competitionId") competitionId: Long): ModelAndView {
        val mav = ModelAndView("competition_ranking")
        mav.model["data"] = competitionRankingService.getCompetitionRankingData(competitionId)
        return mav
    }

    @GetMapping("/ranking/competition/{competitionId}/pdf", produces = [APPLICATION_PDF_VALUE])
    fun getCompetitionRankingAsPdf(@PathVariable("competitionId") competitionId: Long): ResponseEntity<ByteArray> {
        val competitionRanking = competitionRankingService.createCompetitionRanking(competitionId)
        return httpContentUtil.getContentAsPdf("competition_$competitionId.pdf", competitionRanking)
    }

    @GetMapping("/ranking/competition/{competitionId}/events", produces = [APPLICATION_PDF_VALUE])
    fun getEventRanking(@PathVariable("competitionId") competitionId: Long): ResponseEntity<ByteArray> {
        val eventRanking = competitionRankingService.createEventsRanking(competitionId)
        return httpContentUtil.getContentAsPdf("eventsranking_$competitionId.pdf", eventRanking)
    }

    @GetMapping("/ranking/competition/{competitionId}/diplomas", produces = [APPLICATION_PDF_VALUE])
    fun getDiplomas(@PathVariable("competitionId") competitionId: Long): ResponseEntity<ByteArray> {
        val diplomas = competitionRankingService.createDiplomas(competitionId)
        return httpContentUtil.getContentAsPdf("diploma_$competitionId.pdf", diplomas)
    }

    @GetMapping("/ranking/series/{seriesId}")
    fun getSeriesRanking(@PathVariable("seriesId") seriesId: Long): ModelAndView {
        val mav = ModelAndView("series_ranking")
        mav.model["data"] = seriesRankingService.getSeriesRankingData(seriesId)
        return mav
    }

    @GetMapping("/ranking/series/{seriesId}/pdf", produces = [APPLICATION_PDF_VALUE])
    fun getSeriesRankingAsPdf(@PathVariable("seriesId") seriesId: Long): ResponseEntity<ByteArray> {
        val competitionRanking = seriesRankingService.createSeriesRanking(seriesId)
        return httpContentUtil.getContentAsPdf("series_$seriesId.pdf", competitionRanking)
    }

    @GetMapping("/ranking/club/{seriesId}/pdf", produces = [APPLICATION_PDF_VALUE])
    fun getClubRankingAsPdf(@PathVariable("seriesId") seriesId: Long): ResponseEntity<ByteArray> {
        val competitionRanking = clubRankingService.createClubRanking(seriesId)
        return httpContentUtil.getContentAsPdf("clubs_$seriesId.pdf", competitionRanking)
    }

}
