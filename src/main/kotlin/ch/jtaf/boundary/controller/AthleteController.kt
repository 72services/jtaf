package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.ATHLETE
import ch.jtaf.boundary.dto.Message
import ch.jtaf.boundary.security.CheckOrganizationAccess
import ch.jtaf.control.repository.*
import ch.jtaf.entity.Athlete
import ch.jtaf.entity.Club
import ch.jtaf.entity.Organization
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class AthleteController(private val athleteRepository: AthleteRepository,
                        private val clubRepository: ClubRepository,
                        private val categoryRepository: CategoryRepository,
                        private val organizationRepository: OrganizationRepository,
                        private val resultRepository: ResultRepository,
                        private val resultsController: ResultsController) {

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/athlete")
    fun get(@PathVariable organizationKey: String, @RequestParam seriesId: Long?, @RequestParam competitionId: Long?,
            @RequestParam mode: String?, @RequestParam returnTo: String?, model: Model): String {
        if (seriesId != null) {
            model["seriesId"] = seriesId
        }
        if (competitionId != null) {
            model["competitionId"] = competitionId
        }
        if (mode != null) {
            model["mode"] = mode
        }

        model["athlete"] = Athlete()

        val organization = organizationRepository.findByKey(organizationKey)
        model["clubs"] = getClubsWithEmpty(organization)

        return ATHLETE
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/athlete/{athleteId}")
    fun getById(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String, @PathVariable athleteId: Long,
                @RequestParam seriesId: Long?, @RequestParam competitionId: Long?, @RequestParam mode: String?,
                @RequestParam returnTo: String?, model: Model): String {

        if (seriesId != null) {
            model["seriesId"] = seriesId
        }
        if (competitionId != null) {
            model["competitionId"] = competitionId
        }
        if (mode != null) {
            model["mode"] = mode
        }
        if (returnTo != null) {
            model["returnTo"] = returnTo
        }

        model["athlete"] = athleteRepository.getOne(athleteId)

        val organization = organizationRepository.findByKey(organizationKey)
        model["clubs"] = getClubsWithEmpty(organization)

        return ATHLETE
    }

    @CheckOrganizationAccess
    @Transactional
    @PostMapping("/sec/{organizationKey}/athlete")
    fun post(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String,
             @RequestParam seriesId: Long?, @RequestParam competitionId: Long?, @RequestParam mode: String?,
             @RequestParam returnTo: String?, athlete: Athlete, model: Model): String {

        val seriesIds = ArrayList<Long>()
        if (athlete.id != null) {
            val athleteBeforeSave = athleteRepository.getOne(athlete.id!!)
            categoryRepository.findByAthletesId(athlete.id!!).forEach {
                seriesIds.add(it.seriesId!!)
                it.athletes.remove(athleteBeforeSave)
            }

            if (athleteBeforeSave.gender != athlete.gender || athleteBeforeSave.yearOfBirth != athlete.yearOfBirth) {
                resultRepository.deleteResultsFromActiveCompetitions(athlete.id!!)
            }
        }

        val organization = organizationRepository.findByKey(organizationKey)
        athlete.organizationId = organization.id

        athleteRepository.save(athlete)

        seriesIds.forEach {
            val category = categoryRepository.findBySeriesIdAndGenderAndYearFromLessThanEqualAndYearToGreaterThanEqual(
                    it, athlete.gender, athlete.yearOfBirth, athlete.yearOfBirth)
            if (category != null) {
                category.athletes.add(athlete)
                categoryRepository.save(category)
            }
        }

        return if (returnTo == "results") {
            resultsController.getWithAthlete(user, organizationKey, athlete.id!!, seriesId!!, competitionId!!, model)
        } else {
            if (seriesId != null) {
                model["seriesId"] = seriesId
            }
            if (competitionId != null) {
                model["competitionId"] = competitionId
            }
            if (mode != null) {
                model["mode"] = mode
            }
            if (returnTo != null) {
                model["returnTo"] = returnTo
            }

            model["clubs"] = getClubsWithEmpty(organization)

            model["message"] = Message(Message.success, "Athlete saved!")
            model["saved"] = true

            return ATHLETE
        }
    }

    private fun getClubsWithEmpty(organization: Organization): ArrayList<Club?> {
        val clubsFromDb = clubRepository.findByOrganizationIdOrderByAbbreviation(organization.id!!)
        val clubs = ArrayList<Club?>()
        //clubs.add(0, null)
        clubs.addAll(clubsFromDb)
        return clubs
    }
}
