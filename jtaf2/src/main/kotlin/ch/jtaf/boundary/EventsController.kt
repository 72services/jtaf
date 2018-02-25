package ch.jtaf.boundary

import ch.jtaf.control.repository.EventRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/sec/events")
class EventsController(private val eventRepository: EventRepository) {

    @GetMapping
    fun get(@AuthenticationPrincipal user: User, @RequestParam("mode", required = false) mode: String?,
            @RequestParam("categoryId", required = false) categoryId: Long?): ModelAndView {
        val mav = ModelAndView()
        mav.model["events"] = eventRepository.findAllByOwner(user.username)
        mav.model["mode"] = mode ?: "edit"
        if (categoryId != null) {
            mav.model["categoryId"] = categoryId
        }

        return mav
    }

    @GetMapping("{id}/delete")
    fun deleteById(@AuthenticationPrincipal user: User, @PathVariable("id") id: Long): ModelAndView {
        eventRepository.deleteById(id)

        val mav = ModelAndView("/sec/events")
        mav.model["events"] = eventRepository.findAllByOwner(user.username)
        mav.model["mode"] = "edit"
        return mav
    }

}
