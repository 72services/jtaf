package ch.jtaf.boundary

import ch.jtaf.control.repository.ClubRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/sec/clubs")
class ClubsController(private val clubRepository: ClubRepository) {

    @GetMapping
    fun get(@AuthenticationPrincipal user: User): ModelAndView {
        val mav = ModelAndView()
        mav.model["clubs"] = clubRepository.findAllByOwner(user.username)
        return mav
    }
}