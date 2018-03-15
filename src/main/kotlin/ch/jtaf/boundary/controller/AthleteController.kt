package ch.jtaf.boundary.controller

import ch.jtaf.control.repository.*
import ch.jtaf.entity.Athlete
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class AthleteController(private val athleteRepository: AthleteRepository,
                        private val clubRepository: ClubRepository,
                        private val categoryRepository: CategoryRepository,
                        private val organizationRepository: OrganizationRepository,
                        private val resultRepository: ResultRepository,
                        private val resultsController: ResultsController) {

    @GetMapping("/sec/{organization}/athlete")
    fun get(@PathVariable("organization") organizationKey: String,
            @RequestParam("seriesId") seriesId: Long?,
            @RequestParam("competitionId") competitionId: Long?,
            @RequestParam("mode") mode: String?,
            @RequestParam("returnTo") returnTo: String?): ModelAndView {
        val mav = ModelAndView("/sec/athlete")
        mav.model["seriesId"] = seriesId
        mav.model["competitionId"] = competitionId
        mav.model["mode"] = mode

        val athlete = Athlete()
        mav.model["athlete"] = athlete

        val organization = organizationRepository.findByKey(organizationKey)
        mav.model["clubs"] = clubRepository.findByOrganizationId(organization.id!!)

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/sec/{organization}/athlete/{athleteId}")
    fun getById(@AuthenticationPrincipal user: User,
                @PathVariable("organization") organizationKey: String,
                @PathVariable("athleteId") athleteId: Long,
                @RequestParam("seriesId") seriesId: Long?,
                @RequestParam("competitionId") competitionId: Long?,
                @RequestParam("mode") mode: String?,
                @RequestParam("returnTo") returnTo: String?): ModelAndView {
        val mav = ModelAndView("/sec/athlete")
        mav.model["seriesId"] = seriesId
        mav.model["competitionId"] = competitionId
        mav.model["mode"] = mode
        mav.model["returnTo"] = returnTo
        mav.model["returnTo"] = returnTo

        mav.model["athlete"] = athleteRepository.getOne(athleteId)

        val organization = organizationRepository.findByKey(organizationKey)
        mav.model["clubs"] = clubRepository.findByOrganizationId(organization.id!!)

        mav.model["message"] = null
        return mav
    }

    @Transactional
    @PostMapping("/sec/{organization}/athlete")
    fun post(@AuthenticationPrincipal user: User,
             @PathVariable("organization") organizationKey: String,
             @RequestParam("seriesId") seriesId: Long?,
             @RequestParam("competitionId") competitionId: Long?,
             @RequestParam("mode") mode: String?,
             @RequestParam("returnTo") returnTo: String?,
             athlete: Athlete): ModelAndView {

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
            resultsController.getWithAthlete(user, organizationKey, athlete.id!!, seriesId!!, competitionId!!)
        } else {
            val mav = ModelAndView("/sec/athlete")
            mav.model["seriesId"] = seriesId
            mav.model["competitionId"] = competitionId
            mav.model["mode"] = mode
            mav.model["returnTo"] = returnTo

            mav.model["clubs"] = clubRepository.findByOrganizationId(organization.id!!)

            mav.model["message"] = Message(Message.success, "Athlete saved!")
            mav
        }
    }
}
