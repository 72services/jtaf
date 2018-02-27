package ch.jtaf.boundary

import ch.jtaf.control.repository.AthleteRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class AthletesController(private val athleteRepository: AthleteRepository) {

    @GetMapping("/sec/{organization}/athletes")
    fun get(@AuthenticationPrincipal user: User,
            @PathVariable("organization") organzation: String,
            @RequestParam("mode", required = false) mode: String?,
            @RequestParam("seriesId", required = false) seriesId: Long?): ModelAndView {
        val mav = ModelAndView()
        mav.model["athletes"] = athleteRepository.findAllByOwner(user.username)
        mav.model["mode"] = mode ?: "edit"
        if (seriesId != null) {
            mav.model["seriesId"] = seriesId
        }

        return mav
    }

    @GetMapping("/sec/{organization}/athletes/{id}/delete")
    fun deleteById(@AuthenticationPrincipal user: User,
                   @PathVariable("organization") organzation: String,
                   @PathVariable("id") id: Long): ModelAndView {
        athleteRepository.deleteById(id)

        val mav = ModelAndView("/sec/$organzation/athletes")
        mav.model["athletes"] = athleteRepository.findAllByOwner(user.username)
        mav.model["mode"] = "edit"
        return mav
    }
}
