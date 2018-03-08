package ch.jtaf.boundary.controller

import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.ClubRepository
import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.entity.Athlete
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class AthleteController(private val athleteRepository: AthleteRepository,
                        private val clubRepository: ClubRepository,
                        private val organizationRepository: OrganizationRepository,
                        private val resultsController: ResultsController) {

    @GetMapping("/sec/{organization}/athlete")
    fun get(@PathVariable("organization") organizationKey: String,
            @RequestParam("seriesId") seriesId: Long?,
            @RequestParam("competitionId") competitionId: Long?,
            @RequestParam("mode") mode: String?,
            @RequestParam("returnTo") returnTo: String?): ModelAndView {
        val mav = ModelAndView("/sec/athlete")
        mav.model["seriesId"] = seriesId
        mav.model["competitionId"] = competitionId
        mav.model["mode"] = mode

        val athlete = Athlete()
        mav.model["athlete"] = athlete

        val organization = organizationRepository.findByKey(organizationKey)
        mav.model["clubs"] = clubRepository.findByOrganizationId(organization.id!!)

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/sec/{organization}/athlete/{id}")
    fun getById(@AuthenticationPrincipal user: User,
                @PathVariable("organization") organizationKey: String,
                @PathVariable("id") id: Long,
                @RequestParam("seriesId") seriesId: Long?,
                @RequestParam("competitionId") competitionId: Long?,
                @RequestParam("mode") mode: String?,
                @RequestParam("returnTo") returnTo: String?): ModelAndView {
        val mav = ModelAndView("/sec/athlete")
        mav.model["seriesId"] = seriesId
        mav.model["competitionId"] = competitionId
        mav.model["mode"] = mode
        mav.model["returnTo"] = returnTo
        mav.model["returnTo"] = returnTo

        mav.model["athlete"] = athleteRepository.getOne(id)

        val organization = organizationRepository.findByKey(organizationKey)
        mav.model["clubs"] = clubRepository.findByOrganizationId(organization.id!!)

        mav.model["message"] = null
        return mav
    }

    @PostMapping("/sec/{organization}/athlete")
    fun post(@AuthenticationPrincipal user: User,
             @PathVariable("organization") organizationKey: String,
             @RequestParam("seriesId") seriesId: Long?,
             @RequestParam("competitionId") competitionId: Long?,
             @RequestParam("mode") mode: String?,
             @RequestParam("returnTo") returnTo: String?,
             athlete: Athlete): ModelAndView {

        val organization = organizationRepository.findByKey(organizationKey)
        athlete.organizationId = organization.id

        athleteRepository.save(athlete)

        if (returnTo == "results") {
            return resultsController.getWithAthlete(user, organizationKey, athlete.id!!, seriesId!!, competitionId!!)
        } else {
            val mav = ModelAndView("/sec/athlete")
            mav.model["seriesId"] = seriesId
            mav.model["competitionId"] = competitionId
            mav.model["mode"] = mode
            mav.model["returnTo"] = returnTo

            mav.model["clubs"] = clubRepository.findByOrganizationId(organization.id!!)

            mav.model["message"] = Message(Message.success, "Athlete saved!")
            return mav
        }
    }
}
