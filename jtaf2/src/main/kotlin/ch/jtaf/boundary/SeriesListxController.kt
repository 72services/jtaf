package ch.jtaf.boundary

import ch.jtaf.control.repository.SeriesRepository
import ch.jtaf.entity.Gender
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/sec/serieslist")
class SeriesListxController(private val seriesRepository: SeriesRepository) {

    @GetMapping
    fun get(@AuthenticationPrincipal user: User): ModelAndView {
        val mav = ModelAndView()
        mav.model["seriesList"] = seriesRepository.findAllByOwner(user.username)
        return mav
    }
}
