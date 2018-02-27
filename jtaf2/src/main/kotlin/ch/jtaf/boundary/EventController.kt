package ch.jtaf.boundary

import ch.jtaf.control.repository.EventRepository
import ch.jtaf.entity.Event
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class EventController(private val eventRepository: EventRepository) {

    @GetMapping("/sec/{organization}/event")
    fun get(@PathVariable("organization") organization: String): ModelAndView {
        val mav = ModelAndView("/sec/event")
        mav.model["message"] = ""

        val event = Event()
        mav.model["event"] = event

        return mav
    }

    @GetMapping("/sec/{organization}/event/{id}")
    fun getById(@PathVariable("organization") organization: String,
                @PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/event")
        mav.model["message"] = ""

        mav.model["event"] = eventRepository.getOne(id)

        return mav
    }

    @PostMapping("/sec/{organization}/event")
    fun post(@AuthenticationPrincipal user: User,
             @PathVariable("organization") organization: String,
             event: Event): ModelAndView {
        event.owner = user.username

        eventRepository.save(event)

        val mav = ModelAndView("/sec/event")
        mav.model["message"] = "Event saved!"
        return mav
    }
}
