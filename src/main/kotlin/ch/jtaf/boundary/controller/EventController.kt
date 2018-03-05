package ch.jtaf.boundary.controller

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

    @GetMapping("/sec/{organization}/event")
    fun get(@PathVariable("organization") organizationKey: String): ModelAndView {
        val mav = ModelAndView("/sec/event")
        mav.model["message"] = ""

        val event = Event()
        mav.model["event"] = event

        return mav
    }

    @GetMapping("/sec/{organization}/event/{id}")
    fun getById(@PathVariable("organization") organizationKey: String,
                @PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/event")
        mav.model["message"] = ""

        mav.model["event"] = eventRepository.getOne(id)

        return mav
    }

    @PostMapping("/sec/{organization}/event")
    fun post(@AuthenticationPrincipal user: User,
             @PathVariable("organization") organizationKey: String,
             event: Event): ModelAndView {
        val organization = organizationRepository.findByKey(organizationKey)
        event.organizationId = organization.id

        eventRepository.save(event)

        val mav = ModelAndView("/sec/event")
        mav.model["message"] = "Event saved!"
        return mav
    }
}
