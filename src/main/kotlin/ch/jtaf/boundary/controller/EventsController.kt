package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.EVENTS
import ch.jtaf.boundary.security.CheckOrganizationAccess
import ch.jtaf.control.repository.EventRepository
import ch.jtaf.control.repository.OrganizationRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@Controller
class EventsController(private val eventRepository: EventRepository,
                       private val organizationRepository: OrganizationRepository) {

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/events")
    fun get(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String, @RequestParam mode: String?,
            @RequestParam seriesId: Long?, @RequestParam categoryId: Long?, model: Model): String {
        val organization = organizationRepository.findByKey(organizationKey)
        model["events"] = eventRepository.findByOrganizationId(organization.id!!)

        model["mode"] = mode ?: "edit"
        if (seriesId != null) {
            model["seriesId"] = seriesId
        }
        if (categoryId != null) {
            model["categoryId"] = categoryId
        }

        return EVENTS
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/events/{eventId}/delete")
    fun deleteById(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String,
                   @PathVariable("eventId") eventId: Long, model: Model): String {
        eventRepository.deleteById(eventId)

        val organization = organizationRepository.findByKey(organizationKey)
        model["events"] = eventRepository.findByOrganizationId(organization.id!!)

        model["mode"] = "edit"

        return EVENTS
    }

}
