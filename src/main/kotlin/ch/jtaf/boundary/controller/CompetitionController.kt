package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.COMPETITION
import ch.jtaf.boundary.controller.Views.RESULTS
import ch.jtaf.boundary.dto.Message
import ch.jtaf.boundary.dto.ResultContainer
import ch.jtaf.boundary.security.CheckOrganizationAccess
import ch.jtaf.boundary.web.HttpContentProducer
import ch.jtaf.control.repository.CompetitionRepository
import ch.jtaf.control.service.NumberService
import ch.jtaf.control.service.SheetService
import ch.jtaf.entity.AthleteDTO
import ch.jtaf.entity.Competition
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class CompetitionController(private val competitionRepository: CompetitionRepository,
                            private val sheetService: SheetService,
                            private val numberService: NumberService) {

    val httpContentUtil = HttpContentProducer()

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/series/{seriesId}/competition")
    fun get(@PathVariable organizationKey: String, @PathVariable seriesId: Long, model: Model): String {
        val competition = Competition()
        competition.seriesId = seriesId
        model["competition"] = competition

        return COMPETITION
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/series/{seriesId}/competition/{competitionId}")
    fun getById(@PathVariable organizationKey: String, @PathVariable seriesId: Long, @PathVariable competitionId: Long,
                model: Model): String {
        model["competition"] = competitionRepository.getOne(competitionId)
        return COMPETITION
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/series/{seriesId}/competition/{competitionId}/results")
    fun enterResults(@PathVariable organizationKey: String, @PathVariable seriesId: Long, @PathVariable competitionId: Long,
                     model: Model): String {
        model["seriesId"] = seriesId
        model["competitionId"] = competitionId
        model["searchRequest"] = SearchRequest(seriesId = seriesId, competitionId = competitionId)
        model["athletes"] = ArrayList<AthleteDTO>()
        model["resultContainer"] = ResultContainer(seriesId, competitionId, null)

        return RESULTS
    }

    @CheckOrganizationAccess
    @PostMapping("/sec/{organizationKey}/series/{seriesId}/competition")
    fun post(@PathVariable organizationKey: String, @PathVariable seriesId: Long, competition: Competition, model: Model): String {
        competitionRepository.save(competition)

        model["message"] = Message(Message.success, "Competition saved!")

        return COMPETITION
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/series/{seriesId}/competition/{competitionId}/sheets")
    fun getSheets(@PathVariable organizationKey: String, @PathVariable seriesId: Long, @PathVariable competitionId: Long,
                  @RequestParam orderBy: String): ResponseEntity<ByteArray> {

        val sheets = sheetService.createSheets(competitionId, orderBy == "club")
        return httpContentUtil.getContentAsPdf("sheets_$competitionId.pdf", sheets)
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/series/{seriesId}/competition/{competitionId}/numbers")
    fun getNumbers(@PathVariable organizationKey: String, @PathVariable seriesId: Long, @PathVariable competitionId: Long,
                   @RequestParam orderBy: String): ResponseEntity<ByteArray> {

        val numbers = numberService.createNumbers(seriesId, orderBy == "club")
        return httpContentUtil.getContentAsPdf("numbers_$competitionId.pdf", numbers)
    }
}
