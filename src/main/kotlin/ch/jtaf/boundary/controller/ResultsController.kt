package ch.jtaf.boundary.controller

import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.CompetitionRepository
import ch.jtaf.control.repository.ResultRepository
import ch.jtaf.entity.*
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class ResultsController(private val athleteRepository: AthleteRepository,
                        private val competitionRepository: CompetitionRepository,
                        private val resultRepository: ResultRepository,
                        private val categoryRepository: CategoryRepository) {

    @PostMapping("/sec/{organization}/search")
    fun search(@AuthenticationPrincipal user: User,
               @PathVariable("organization") organizationKey: String,
               searchRequest: SearchRequest): ModelAndView {
        val id = searchRequest.term.toLongOrNull()
        if (id != null) {
            return getWithAthlete(user, organizationKey, id, searchRequest.seriesId, searchRequest.competitionId)
        } else {
            val mav = ModelAndView("/sec/athlete_results")
            mav.model["seriesId"] = searchRequest.seriesId
            mav.model["competitionId"] = searchRequest.competitionId

            mav.model["searchRequest"] = searchRequest

            val athletes = athleteRepository.searchAthletes(searchRequest.seriesId, searchRequest.term + "%")
            mav.model["athletes"] = athletes
            mav.model["athlete"] = null
            mav.model["resultContainer"] = ResultContainer(searchRequest.seriesId, searchRequest.competitionId, null)

            mav.model["message"] = null
            return mav
        }
    }

    @GetMapping("/sec/{organization}/athlete/{athleteId}/results")
    fun getWithAthlete(@AuthenticationPrincipal user: User,
                       @PathVariable("organization") organizationKey: String,
                       @PathVariable("athleteId") athleteId: Long,
                       @RequestParam("seriesId") seriesId: Long,
                       @RequestParam("competitionId") competitionId: Long): ModelAndView {

        val athlete = athleteRepository.getOne(athleteId)
        val competition = competitionRepository.getOne(competitionId)

        val category = categoryRepository.findBySeriesIdAndGenderAndYearFromLessThanEqualAndYearToGreaterThanEqual(
                seriesId, athlete.gender, athlete.yearOfBirth, athlete.yearOfBirth)
                ?: throw IllegalStateException("No matching category found for athlete " + athlete.id)

        val results = resultRepository.findByAthleteIdAndCompetitionId(athleteId, competitionId)

        checkIfResultsCompleteOrAddMissingResults(category, athlete, competition, results)

        val mav = ModelAndView("/sec/athlete_results")
        mav.model["seriesId"] = seriesId
        mav.model["competitionId"] = competitionId
        mav.model["searchRequest"] = SearchRequest(seriesId = seriesId, competitionId = competitionId, term = athleteId.toString())
        mav.model["athletes"] = ArrayList<AthleteDTO>()
        mav.model["athlete"] = athleteRepository.getOneAthleteDTO(athleteId)
        mav.model["resultContainer"] = ResultContainer(seriesId, competitionId, athlete.id, results)

        mav.model["message"] = null
        return mav
    }

    @PostMapping("/sec/{organization}/results")
    fun postResults(@AuthenticationPrincipal user: User,
                    @PathVariable("organization") organizationKey: String,
                    resultContainer: ResultContainer): ModelAndView {
        val savedResults = ArrayList<Result>()
        resultContainer.results.forEach {
            val result = resultRepository.getOne(it.id!!)
            result.result = it.result
            val savedResult = resultRepository.save(result)
            savedResults.add(savedResult)
        }
        resultContainer.results = savedResults

        val mav = ModelAndView("/sec/athlete_results")
        mav.model["seriesId"] = null
        mav.model["competitionId"] = null
        mav.model["searchRequest"] = SearchRequest(resultContainer.seriesId, resultContainer.competitionId)
        mav.model["athletes"] = ArrayList<AthleteDTO>()
        mav.model["athlete"] = athleteRepository.getOneAthleteDTO(resultContainer.athleteId!!)
        mav.model["resultContainer"] = resultContainer

        mav.model["message"] = Message(Message.success, "Result saved!")
        return mav
    }

    private fun checkIfResultsCompleteOrAddMissingResults(category: Category, athlete: Athlete, competition: Competition, results: MutableList<Result>) {
        if (category.events.size == results.size) {
            // TODO check if the results have the appropriate event type!
            return
        } else {
            val newResults = ArrayList<Result>()
            category.events.forEach { event: Event ->
                if (!results.any { it.event == event }) {
                    val result = Result()
                    result.event = event
                    result.athlete = athlete
                    result.competition = competition
                    result.position = event.position

                    resultRepository.save(result)
                    newResults.add(result)
                }
            }
            results.addAll(results)
        }
    }

}