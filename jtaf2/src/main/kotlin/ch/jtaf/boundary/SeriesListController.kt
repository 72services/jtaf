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
class SeriesListController(private val seriesRepository: SeriesRepository,
                           private val organizationAuthorizationChecker: OrganizationAuthorizationChecker) {

    @GetMapping("/sec/{organization}/serieslist")
    fun get(@AuthenticationPrincipal user: User,
            @PathVariable("organization") organization: String): ModelAndView {
        val mav = ModelAndView("/sec/serieslist")
        mav.model["seriesList"] = seriesRepository.findAllByOwner(user.username)
        return mav
    }

    @GetMapping("/sec/{organization}/serieslist/{id}/delete")
    fun deleteById(@AuthenticationPrincipal user: User,
                   @PathVariable("organization") organization: String,
                   @PathVariable("id") id: Long): ModelAndView {
        seriesRepository.deleteById(id)

        val mav = ModelAndView("/sec/serieslist")
        mav.model["seriesList"] = seriesRepository.findAllByOwner(user.username)
        return mav
    }

}
