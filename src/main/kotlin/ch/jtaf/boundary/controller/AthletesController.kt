package ch.jtaf.boundary.controller

import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.OrganizationRepository
import ch.jtaf.control.repository.SeriesRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class AthletesController(private val athleteRepository: AthleteRepository,
                         private val organizationRepository: OrganizationRepository,
                         private val seriesRepository: SeriesRepository,
                         private val categoryRepository: CategoryRepository) {

    @GetMapping("/sec/{organizationKey}/athletes")
    fun get(@AuthenticationPrincipal user: User,
            @PathVariable("organizationKey") organizationKey: String,
            @RequestParam("mode") mode: String?,
            @RequestParam("seriesId") seriesId: Long?): ModelAndView {

        val mav = ModelAndView("sec/athletes")

        val organization = organizationRepository.findByKey(organizationKey)

        mav.model["athletes"] = if (mode == "select") {
            athleteRepository.findByOrganizationIdAndNotAssignedToSeries(organization.id!!, seriesId)
        } else {
            athleteRepository.findByOrganizationIdOrderByLastNameAscFirstNameAsc(organization.id!!)
        }

        mav.model["mode"] = mode ?: "edit"
        if (seriesId != null) {
            mav.model["seriesId"] = seriesId
        }

        return mav
    }

    @GetMapping("/sec/{organizationKey}/athletes/{athleteId}/delete")
    fun deleteById(@AuthenticationPrincipal user: User,
                   @PathVariable("organizationKey") organizationKey: String,
                   @PathVariable("athleteId") athleteId: Long): ModelAndView {

        athleteRepository.deleteById(athleteId)

        val mav = ModelAndView("sec/athletes")

        val organization = organizationRepository.findByKey(organizationKey)
        mav.model["athletes"] = athleteRepository.findByOrganizationIdOrderByLastNameAscFirstNameAsc(organization.id!!)

        mav.model["mode"] = "edit"
        return mav
    }

    @GetMapping("/sec/{organizationKey}/athletes/{athleteId}/series/{seriesId}")
    fun addEvent(@AuthenticationPrincipal user: User,
                 @PathVariable("organizationKey") organizationKey: String,
                 @PathVariable("seriesId") seriesId: Long,
                 @PathVariable("athleteId") athleteId: Long): ModelAndView {

        val athlete = athleteRepository.getOne(athleteId)
        val category = categoryRepository.findBySeriesIdAndGenderAndYearFromLessThanEqualAndYearToGreaterThanEqual(
                seriesId, athlete.gender, athlete.yearOfBirth, athlete.yearOfBirth)

        if (category == null) {
            throw IllegalStateException("No matching category found for gender " + athlete.gender + " and year " + athlete.yearOfBirth)
        } else {
            category.athletes.add(athlete)
            categoryRepository.save(category)
        }

        return get(user, organizationKey, "select", seriesId)
    }

}
