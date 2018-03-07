package ch.jtaf.boundary.controller

import ch.jtaf.control.reporting.report.CompetitionRanking
import ch.jtaf.control.repository.EventRepository
import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.entity.Event
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class RankingController(private val eventRepository: EventRepository,
                        private val organizationRepository: OrganizationRepository) {

    @GetMapping("/ranking/competition/{id}")
    fun get(@PathVariable("id") competitionId: Long): ModelAndView {
        val mav = ModelAndView("/ranking")

        mav.model["ranking"] = null

        mav.model["message"] = null
        return mav
    }

}
