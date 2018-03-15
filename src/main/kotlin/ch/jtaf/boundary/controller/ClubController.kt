package ch.jtaf.boundary.controller

import ch.jtaf.control.repository.ClubRepository
import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.entity.Club
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class ClubController(private val clubRepository: ClubRepository,
                     private val organizationRepository: OrganizationRepository) {

    @GetMapping("/sec/{organization}/club")
    fun get(@PathVariable("organization") organizationKey: String): ModelAndView {
        val mav = ModelAndView("/sec/club")

        val club = Club()
        mav.model["club"] = club

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/sec/{organization}/club/{clubId}")
    fun getById(@PathVariable("organization") organizationKey: String,
                @PathVariable("clubId") clubId: Long): ModelAndView {
        val mav = ModelAndView("/sec/club")

        mav.model["club"] = clubRepository.getOne(clubId)

        mav.model["message"] = null
        return mav
    }

    @PostMapping("/sec/{organization}/club")
    fun post(@AuthenticationPrincipal user: User,
             @PathVariable("organization") organizationKey: String,
             club: Club): ModelAndView {
        val organization = organizationRepository.findByKey(organizationKey)
        club.organizationId = organization.id

        clubRepository.save(club)

        val mav = ModelAndView("/sec/club")

        mav.model["message"] = Message(Message.success, "Club saved!")
        return mav
    }
}
