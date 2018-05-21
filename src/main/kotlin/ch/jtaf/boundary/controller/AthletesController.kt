package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.ATHLETES
import ch.jtaf.boundary.security.CheckOrganizationAccess
import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.control.repository.SeriesRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@Controller
class AthletesController(private val athleteRepository: AthleteRepository,
                         private val organizationRepository: OrganizationRepository,
                         private val categoryRepository: CategoryRepository,
                         private val seriesListController: SeriesListController) {

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/athletes")
    fun get(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String, @RequestParam mode: String?,
            @RequestParam seriesId: Long?, model: Model): String {

        val organization = organizationRepository.findByKey(organizationKey)

        model["athletes"] = if (mode == "select") {
            athleteRepository.findByOrganizationIdAndNotAssignedToSeries(organization.id!!, seriesId)
        } else {
            athleteRepository.findByOrganizationIdOrderByLastNameAscFirstNameAsc(organization.id!!)
        }

        model["mode"] = mode ?: "edit"
        if (seriesId != null) {
            model["seriesId"] = seriesId
        }

        return ATHLETES
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/athletes/{athleteId}/delete")
    fun deleteById(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String, @PathVariable athleteId: Long,
                   model: Model): String {

        athleteRepository.deleteById(athleteId)

        val organization = organizationRepository.findByKey(organizationKey)
        model["athletes"] = athleteRepository.findByOrganizationIdOrderByLastNameAscFirstNameAsc(organization.id!!)

        model["mode"] = "edit"

        return ATHLETES
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/athletes/{athleteId}/series/{seriesId}")
    fun addEvent(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String,
                 @PathVariable seriesId: Long, @PathVariable athleteId: Long, @RequestParam returnTo: String?,
                 model: Model): String {

        val athlete = athleteRepository.getOne(athleteId)
        val category = categoryRepository.findBySeriesIdAndGenderAndYearFromLessThanEqualAndYearToGreaterThanEqual(
                seriesId, athlete.gender, athlete.yearOfBirth, athlete.yearOfBirth)

        if (category == null) {
            throw IllegalStateException("No matching category found for gender " + athlete.gender + " and year " + athlete.yearOfBirth)
        } else {
            category.athletes.add(athlete)
            categoryRepository.save(category)
        }

        if (returnTo != null && returnTo == "serieslist") {
            return seriesListController.get(user, organizationKey, model)
        } else {
            return get(user, organizationKey, "select", seriesId, model)
        }
    }

}
