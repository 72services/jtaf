package ch.jtaf.boundary

import ch.jtaf.control.repository.SeriesRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/sec/serieslist")
class SeriesListController(private val seriesRepository: SeriesRepository,
                           private val seriesAuthorizationChecker: SeriesAuthorizationChecker) {

    @GetMapping
    fun get(@AuthenticationPrincipal user: User): ModelAndView {
        val mav = ModelAndView()
        mav.model["seriesList"] = seriesRepository.findAllByOwner(user.username)
        return mav
    }

    @GetMapping("{id}/delete")
    fun deleteById(@AuthenticationPrincipal user: User, @PathVariable("id") id: Long): ModelAndView {
        seriesAuthorizationChecker.checkIfUserAccessToSeries(id)

        seriesRepository.deleteById(id)

        val mav = ModelAndView("/sec/serieslist")
        mav.model["seriesList"] = seriesRepository.findAllByOwner(user.username)
        return mav
    }

}
