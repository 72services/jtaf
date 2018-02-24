package ch.jtaf.boundary

import ch.jtaf.control.repository.CompetitionRepository
import ch.jtaf.entity.Competition
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/sec/competition")
class CompetitionController(private val competitionRepository: CompetitionRepository,
                            private val seriesAuthorizationChecker: SeriesAuthorizationChecker) {

    @GetMapping()
    fun get(@RequestParam("seriesId") seriesId: Long): ModelAndView {
        seriesAuthorizationChecker.checkIfUserAccessToSeries(seriesId)

        val mav = ModelAndView("/sec/competition")
        mav.model["message"] = ""

        val competition = Competition()
        competition.seriesId = seriesId
        mav.model["competition"] = competition

        return mav
    }

    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/competition")
        mav.model["message"] = ""

        val competition = competitionRepository.findById(id)
        if (competition.isPresent) {
            mav.model["competition"] = competition.get()
        } else {
            throw IllegalStateException("Competition not found")
        }

        return mav
    }

    @PostMapping
    fun post(competition: Competition): ModelAndView {
        seriesAuthorizationChecker.checkIfUserAccessToSeries(competition.seriesId)

        competitionRepository.save(competition)

        val mav = ModelAndView()
        mav.model["message"] = "Competition saved!"
        return mav
    }
}
