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


@Controller
class RankingController(private val rankingService: CompetitionRankingService) {

    @GetMapping("/ranking/competition/{id}", produces = arrayOf(APPLICATION_PDF_VALUE))
    fun get(@PathVariable("id") competitionId: Long): ResponseEntity<ByteArray> {
        val competitionRanking = rankingService.createCompetitionRanking(competitionId)

        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("application/pdf")
        val filename = "competition_$competitionId.pdf"
        headers.setContentDispositionFormData(filename, filename)
        headers.cacheControl = "must-revalidate, post-check=0, pre-check=0"

        return ResponseEntity(competitionRanking, headers, HttpStatus.OK)
    }

}
