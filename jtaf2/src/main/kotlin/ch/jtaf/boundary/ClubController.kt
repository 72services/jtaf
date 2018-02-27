package ch.jtaf.boundary

import ch.jtaf.control.repository.ClubRepository
import ch.jtaf.entity.Club
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class ClubController(private val clubRepository: ClubRepository) {

    @GetMapping("/sec/{organization}/club")
    fun get(@PathVariable("organization") organzation: String): ModelAndView {
        val mav = ModelAndView("/sec/${organzation}/club")
        mav.model["message"] = ""

        val club = Club()
        mav.model["club"] = club

        return mav
    }

    @GetMapping("/sec/{organization}/club/{id}")
    fun getById(@PathVariable("organization") organzation: String,
                @PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/${organzation}/club")
        mav.model["message"] = ""

        mav.model["club"] = clubRepository.getOne(id)

        return mav
    }

    @PostMapping("/sec/{organization}/club")
    fun post(@AuthenticationPrincipal user: User,
             @PathVariable("organization") organzation: String,
             club: Club): ModelAndView {
        club.owner = user.username

        clubRepository.save(club)

        val mav = ModelAndView()
        mav.model["message"] = "Club saved!"
        return mav
    }
}
