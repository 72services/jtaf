package ch.jtaf.boundary

import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.ClubRepository
import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.entity.Athlete
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
class AthleteController(private val athleteRepository: AthleteRepository,
                        private val clubRepository: ClubRepository,
                        private val organizationRepository: OrganizationRepository) {

    @GetMapping("/sec/{organization}/athlete")
    fun get(@PathVariable("organization") organizationKey: String,
            @RequestParam("seriesId") seriesId: Long,
            @RequestParam("mode") mode: String): ModelAndView {
        val mav = ModelAndView("/sec/athlete")
        mav.model["message"] = ""
        mav.model["seriesId"] = seriesId
        mav.model["mode"] = mode

        val athlete = Athlete()
        mav.model["athlete"] = athlete
        mav.model["clubs"] = clubRepository.findAll()

        return mav
    }

    @GetMapping("/sec/{organization}/athlete/{id}")
    fun getById(@AuthenticationPrincipal user: User,
                @PathVariable("organization") organizationKey: String,
                @PathVariable("id") id: Long,
                @RequestParam("seriesId") seriesId: Long,
                @RequestParam("mode") mode: String): ModelAndView {
        val mav = ModelAndView("/sec/athlete")
        mav.model["message"] = ""
        mav.model["seriesId"] = seriesId
        mav.model["mode"] = mode

        mav.model["athlete"] = athleteRepository.getOne(id)

        val organization = organizationRepository.findByKey(organizationKey)
        mav.model["clubs"] = clubRepository.findByOrganizationId(organization.id!!)

        return mav
    }

    @PostMapping("/sec/{organization}/athlete")
    fun post(@AuthenticationPrincipal user: User,
             @PathVariable("organization") organizationKey: String,
             @RequestParam("seriesId") seriesId: Long,
             @RequestParam("mode") mode: String,
             athlete: Athlete): ModelAndView {

        val organization = organizationRepository.findByKey(organizationKey)
        athlete.organizationId = organization.id

        athleteRepository.save(athlete)

        val mav = ModelAndView("/sec/athlete")
        mav.model["seriesId"] = seriesId
        mav.model["mode"] = mode

        mav.model["message"] = "Athlete saved!"
        mav.model["clubs"] = clubRepository.findAll()

        return mav
    }
}
