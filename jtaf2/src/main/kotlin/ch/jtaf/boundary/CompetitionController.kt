package ch.jtaf.boundary

import ch.jtaf.control.repository.CompetitionRepository
import ch.jtaf.control.repository.SeriesRepository
import ch.jtaf.entity.Competition
import ch.jtaf.entity.Series
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/sec/competition")
class CompetitionController(private val competitionRepository: CompetitionRepository,
                            private val seriesRepository: SeriesRepository) {

    @GetMapping()
    fun get(@AuthenticationPrincipal user: User, @RequestParam("seriesId") seriesId: Long): ModelAndView {
        val series = seriesRepository.findById(seriesId)
        if (series.isPresent) {
            if (series.get().owner == user.username) {
                val mav = ModelAndView("/sec/competition")
                mav.model["message"] = ""

                val competition = Competition()
                competition.seriesId = series.get().id
                mav.model["competition"] = competition

                return mav
            } else {
                throw IllegalArgumentException("User is not owner of the series")
            }
        } else {
            throw IllegalArgumentException("Series not found")
        }
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
        competitionRepository.save(competition)

        val mav = ModelAndView()
        mav.model["message"] = "Competition saved!"
        return mav
    }
}
