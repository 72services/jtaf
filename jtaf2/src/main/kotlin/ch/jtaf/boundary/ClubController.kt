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
@RequestMapping("/sec/club")
class ClubController(private val clubRepository: ClubRepository) {

    @GetMapping()
    fun get(): ModelAndView {
        val mav = ModelAndView("/sec/club")
        mav.model["message"] = ""

        val club = Club()
        mav.model["club"] = club

        return mav
    }

    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/club")
        mav.model["message"] = ""

        val club = clubRepository.findById(id)
        if (club.isPresent) {
            mav.model["club"] = club.get()
        } else {
            throw IllegalStateException("Club not found")
        }

        return mav
    }

    @PostMapping
    fun post(@AuthenticationPrincipal user: User, club: Club): ModelAndView {
        club.owner = user.username

        clubRepository.save(club)

        val mav = ModelAndView()
        mav.model["message"] = "Club saved!"
        return mav
    }
}
