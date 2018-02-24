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
@RequestMapping("/sec/event")
class EventController(private val eventRepository: EventRepository) {

    @GetMapping()
    fun get(): ModelAndView {
        val mav = ModelAndView("/sec/event")
        mav.model["message"] = ""

        val event = Event()
        mav.model["event"] = event

        return mav
    }

    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/event")
        mav.model["message"] = ""

        val event = eventRepository.findById(id)
        if (event.isPresent) {
            mav.model["event"] = event.get()
        } else {
            throw IllegalStateException("Event not found")
        }

        return mav
    }

    @PostMapping
    fun post(@AuthenticationPrincipal user: User, event: Event): ModelAndView {
        event.owner = user.username

        eventRepository.save(event)

        val mav = ModelAndView()
        mav.model["message"] = "Event saved!"
        return mav
    }
}
