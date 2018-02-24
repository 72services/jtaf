package ch.jtaf.boundary

import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.entity.Athlete
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/sec/athlete")
class AthleteController(private val athleteRepository: AthleteRepository) {

    @GetMapping()
    fun get(): ModelAndView {
        val mav = ModelAndView("/sec/athlete")
        mav.model["message"] = ""

        val athlete = Athlete()
        mav.model["athlete"] = athlete

        return mav
    }

    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/athlete")
        mav.model["message"] = ""

        val athlete = athleteRepository.findById(id)
        if (athlete.isPresent) {
            mav.model["athlete"] = athlete.get()
        } else {
            throw IllegalStateException("Athlete not found")
        }

        return mav
    }

    @PostMapping
    fun post(athlete: Athlete): ModelAndView {
        athleteRepository.save(athlete)

        val mav = ModelAndView()
        mav.model["message"] = "Athlete saved!"
        return mav
    }
}
