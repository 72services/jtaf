package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.EVENT
import ch.jtaf.boundary.dto.Message
import ch.jtaf.boundary.security.CheckOrganizationAccess
import ch.jtaf.control.repository.EventRepository
import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.entity.Event
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class EventController(private val eventRepository: EventRepository,
                      private val organizationRepository: OrganizationRepository) {

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/event")
    fun get(@PathVariable organizationKey: String, model: Model): String {
        model["event"] = Event()
        return EVENT
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/event/{eventId}")
    fun getById(@PathVariable organizationKey: String, @PathVariable eventId: Long, model: Model): String {
        model["event"] = eventRepository.getOne(eventId)
        return EVENT
    }

    @CheckOrganizationAccess
    @PostMapping("/sec/{organizationKey}/event")
    fun post(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String, event: Event, model: Model): String {
        val organization = organizationRepository.findByKey(organizationKey)
        event.organizationId = organization.id

        eventRepository.save(event)

        model["message"] = Message(Message.success, "Event saved!")

        return EVENT
    }
}
