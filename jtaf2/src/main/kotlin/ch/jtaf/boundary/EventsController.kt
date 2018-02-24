package ch.jtaf.boundary

import ch.jtaf.control.repository.EventRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/sec/events")
class EventsController(private val eventRepository: EventRepository) {

    @GetMapping
    fun get(@AuthenticationPrincipal user: User): ModelAndView {
        val mav = ModelAndView()
        mav.model["events"] = eventRepository.findAllByOwner(user.username)
        return mav
    }
}
