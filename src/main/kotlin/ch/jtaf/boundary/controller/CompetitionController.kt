package ch.jtaf.boundary.controller

import ch.jtaf.control.repository.CompetitionRepository
import ch.jtaf.entity.AthleteDTO
import ch.jtaf.entity.Competition
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class CompetitionController(private val competitionRepository: CompetitionRepository,
                            private val organizationAuthorizationChecker: OrganizationAuthorizationChecker) {

    @GetMapping("/sec/{organization}/series/{seriesId}/competition")
    fun get(@PathVariable("organization") organizationKey: String,
            @PathVariable("seriesId") seriesId: Long): ModelAndView {
        val mav = ModelAndView("/sec/competition")

        val competition = Competition()
        competition.seriesId = seriesId
        mav.model["competition"] = competition

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/sec/{organization}/series/{seriesId}/competition/{id}")
    fun getById(@PathVariable("organization") organizationKey: String,
                @PathVariable("seriesId") seriesId: Long,
                @PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/competition")

        mav.model["competition"] = competitionRepository.getOne(id)

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/sec/{organization}/series/{seriesId}/competition/{id}/results")
    fun enterResults(@PathVariable("organization") organizationKey: String,
                     @PathVariable("seriesId") seriesId: Long,
                     @PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/athlete_results")
        mav.model["seriesId"] = seriesId
        mav.model["competitionId"] = id
        mav.model["searchRequest"] = SearchRequest(seriesId = seriesId, competitionId = id)
        mav.model["athletes"] = ArrayList<AthleteDTO>()
        mav.model["athlete"] = null
        mav.model["resultContainer"] = ResultContainer(seriesId, id, null)

        mav.model["message"] = null
        return mav
    }

    @PostMapping("/sec/{organization}/series/{seriesId}/competition")
    fun post(@PathVariable("organization") organizationKey: String,
             @PathVariable("seriesId") seriesId: Long,
             competition: Competition): ModelAndView {
        competitionRepository.save(competition)

        val mav = ModelAndView("/sec/competition")
        mav.model["message"] = Message(Message.success, "Competition saved!")
        return mav
    }
}
