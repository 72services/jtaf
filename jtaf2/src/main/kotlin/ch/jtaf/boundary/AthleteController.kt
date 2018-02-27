package ch.jtaf.boundary

import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.ClubRepository
import ch.jtaf.entity.Athlete
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class AthleteController(private val athleteRepository: AthleteRepository,
                        private val clubRepository: ClubRepository) {

    @GetMapping("/sec/{organization}/athlete")
    fun get(@PathVariable("organization") organzation: String): ModelAndView {
        val mav = ModelAndView("/sec/$organzation/athlete")
        mav.model["message"] = ""

        val athlete = Athlete()
        mav.model["athlete"] = athlete
        mav.model["clubs"] = clubRepository.findAll()

        return mav
    }

    @GetMapping("/sec/{organization}/athlete/{id}")
    fun getById(@AuthenticationPrincipal user: User,
                @PathVariable("organization") organzation: String,
                @PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/${organzation}athlete")
        mav.model["message"] = ""

        mav.model["athlete"] = athleteRepository.getOne(id)
        mav.model["clubs"] = clubRepository.findAllByOwner(user.username)

        return mav
    }

    @PostMapping("/sec/{organization}/athlete")
    fun post(@AuthenticationPrincipal user: User,
             @PathVariable("organization") organzation: String,
             athlete: Athlete): ModelAndView {
        athlete.owner = user.username

        athleteRepository.save(athlete)

        val mav = ModelAndView()
        mav.model["message"] = "Athlete saved!"
        mav.model["clubs"] = clubRepository.findAll()

        return mav
    }
}
