package ch.jtaf.boundary.controller

import ch.jtaf.boundary.dto.Message
import ch.jtaf.control.repository.EventRepository
import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.entity.Event
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class EventController(private val eventRepository: EventRepository,
                      private val organizationRepository: OrganizationRepository) {

    @GetMapping("/sec/{organizationKey}/event")
    fun get(@PathVariable("organizationKey") organizationKey: String): ModelAndView {
        val mav = ModelAndView("/sec/event")

        val event = Event()
        mav.model["event"] = event

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/sec/{organizationKey}/event/{eventId}")
    fun getById(@PathVariable("organizationKey") organizationKey: String,
                @PathVariable("eventId") eventId: Long): ModelAndView {
        val mav = ModelAndView("/sec/event")

        mav.model["event"] = eventRepository.getOne(eventId)

        mav.model["message"] = null
        return mav
    }

    @PostMapping("/sec/{organizationKey}/event")
    fun post(@AuthenticationPrincipal user: User,
             @PathVariable("organizationKey") organizationKey: String,
             event: Event): ModelAndView {
        val organization = organizationRepository.findByKey(organizationKey)
        event.organizationId = organization.id

        eventRepository.save(event)

        val mav = ModelAndView("/sec/event")
        mav.model["message"] = Message(Message.success, "Event saved!")
        return mav
    }
}
