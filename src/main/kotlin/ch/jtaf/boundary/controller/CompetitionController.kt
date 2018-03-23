package ch.jtaf.boundary.controller

import ch.jtaf.boundary.dto.Message
import ch.jtaf.boundary.dto.ResultContainer
import ch.jtaf.boundary.web.HttpContentProducer
import ch.jtaf.control.repository.CompetitionRepository
import ch.jtaf.control.service.NumberService
import ch.jtaf.control.service.SheetService
import ch.jtaf.entity.AthleteDTO
import ch.jtaf.entity.Competition
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class CompetitionController(private val competitionRepository: CompetitionRepository,
                            private val sheetService: SheetService,
                            private val numberService: NumberService) {

    val httpContentUtil = HttpContentProducer()

    @GetMapping("/sec/{organizationKey}/series/{seriesId}/competition")
    fun get(@PathVariable("organizationKey") organizationKey: String,
            @PathVariable("seriesId") seriesId: Long): ModelAndView {
        val mav = ModelAndView("sec/competition")

        val competition = Competition()
        competition.seriesId = seriesId
        mav.model["competition"] = competition

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/sec/{organizationKey}/series/{seriesId}/competition/{competitionId}")
    fun getById(@PathVariable("organizationKey") organizationKey: String,
                @PathVariable("seriesId") seriesId: Long,
                @PathVariable("competitionId") competitionId: Long): ModelAndView {
        val mav = ModelAndView("sec/competition")

        mav.model["competition"] = competitionRepository.getOne(competitionId)

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/sec/{organizationKey}/series/{seriesId}/competition/{competitionId}/results")
    fun enterResults(@PathVariable("organizationKey") organizationKey: String,
                     @PathVariable("seriesId") seriesId: Long,
                     @PathVariable("competitionId") competitionId: Long): ModelAndView {
        val mav = ModelAndView("sec/results")
        mav.model["seriesId"] = seriesId
        mav.model["competitionId"] = competitionId
        mav.model["searchRequest"] = SearchRequest(seriesId = seriesId, competitionId = competitionId)
        mav.model["athletes"] = ArrayList<AthleteDTO>()
        mav.model["athlete"] = null
        mav.model["resultContainer"] = ResultContainer(seriesId, competitionId, null)

        mav.model["message"] = null
        return mav
    }

    @PostMapping("/sec/{organizationKey}/series/{seriesId}/competition")
    fun post(@PathVariable("organizationKey") organizationKey: String,
             @PathVariable("seriesId") seriesId: Long,
             competition: Competition): ModelAndView {
        competitionRepository.save(competition)

        val mav = ModelAndView("sec/competition")
        mav.model["message"] = Message(Message.success, "Competition saved!")
        return mav
    }

    @GetMapping("/sec/{organizationKey}/series/{seriesId}/competition/{competitionId}/sheets")
    fun getSheets(@PathVariable("organizationKey") organizationKey: String,
                  @PathVariable("seriesId") seriesId: Long,
                  @PathVariable("competitionId") competitionId: Long,
                  @RequestParam("orderBy") orderBy: String): ResponseEntity<ByteArray> {

        val sheets = sheetService.createSheets(competitionId, orderBy == "club")
        return httpContentUtil.getContentAsPdf("sheets_$competitionId.pdf", sheets)
    }

    @GetMapping("/sec/{organizationKey}/series/{seriesId}/competition/{competitionId}/numbers")
    fun getNumbers(@PathVariable("organizationKey") organizationKey: String,
                   @PathVariable("seriesId") seriesId: Long,
                   @PathVariable("competitionId") competitionId: Long,
                   @RequestParam("orderBy") orderBy: String): ResponseEntity<ByteArray> {

        val numbers = numberService.createNumbers(seriesId, orderBy == "club")
        return httpContentUtil.getContentAsPdf("numbers_$competitionId.pdf", numbers)
    }
}
