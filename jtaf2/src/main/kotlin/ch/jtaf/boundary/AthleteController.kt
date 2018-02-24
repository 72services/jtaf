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
@RequestMapping("/sec/athlete")
class AthleteController(private val athleteRepository: AthleteRepository,
                        private val clubRepository: ClubRepository) {

    @GetMapping()
    fun get(): ModelAndView {
        val mav = ModelAndView("/sec/athlete")
        mav.model["message"] = ""

        val athlete = Athlete()
        mav.model["athlete"] = athlete
        mav.model["clubs"] = clubRepository.findAll()

        return mav
    }

    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/athlete")
        mav.model["message"] = ""

        val athlete = athleteRepository.findById(id)
        if (athlete.isPresent) {
            mav.model["athlete"] = athlete.get()
            mav.model["clubs"] = clubRepository.findAll()
        } else {
            throw IllegalStateException("Athlete not found")
        }

        return mav
    }

    @PostMapping
    fun post(@AuthenticationPrincipal user: User, athlete: Athlete): ModelAndView {
        athlete.owner = user.username

        athleteRepository.save(athlete)

        val mav = ModelAndView()
        mav.model["message"] = "Athlete saved!"
        mav.model["clubs"] = clubRepository.findAll()

        return mav
    }
}
