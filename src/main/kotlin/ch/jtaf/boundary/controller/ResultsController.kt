package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.RESULTS
import ch.jtaf.boundary.dto.Message
import ch.jtaf.boundary.dto.ResultContainer
import ch.jtaf.boundary.security.CheckOrganizationAccess
import ch.jtaf.control.repository.AthleteRepository
import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.CompetitionRepository
import ch.jtaf.control.repository.ResultRepository
import ch.jtaf.entity.*
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class ResultsController(private val athleteRepository: AthleteRepository,
                        private val competitionRepository: CompetitionRepository,
                        private val resultRepository: ResultRepository,
                        private val categoryRepository: CategoryRepository) {

    @CheckOrganizationAccess
    @PostMapping("/sec/{organizationKey}/search")
    fun search(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String, searchRequest: SearchRequest,
               model: Model): String {
        val id = searchRequest.term.toLongOrNull()
        if (id != null) {
            return getWithAthlete(user, organizationKey, id, searchRequest.seriesId, searchRequest.competitionId, model)
        } else {
            val athletes = athleteRepository.searchAthletes(searchRequest.seriesId, searchRequest.term + "%")
            if (athletes.size == 1) {
                return getWithAthlete(user, organizationKey, athletes[0].id, searchRequest.seriesId, searchRequest.competitionId, model)
            } else {
                model["seriesId"] = searchRequest.seriesId
                model["competitionId"] = searchRequest.competitionId

                model["searchRequest"] = searchRequest

                model["athletes"] = athletes
                model["resultContainer"] = ResultContainer(searchRequest.seriesId, searchRequest.competitionId, null)

                return RESULTS
            }
        }
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/athlete/{athleteId}/results")
    fun getWithAthlete(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String, @PathVariable athleteId: Long,
                       @RequestParam seriesId: Long, @RequestParam competitionId: Long, model: Model): String {

        val athlete = athleteRepository.getOne(athleteId)
        val competition = competitionRepository.getOne(competitionId)

        val category = categoryRepository.findBySeriesIdAndGenderAndYearFromLessThanEqualAndYearToGreaterThanEqual(
                seriesId, athlete.gender, athlete.yearOfBirth, athlete.yearOfBirth)
                ?: throw IllegalStateException("No matching category found for athlete " + athlete.id)

        val results = resultRepository.findByAthleteIdAndCompetitionIdOrderByPosition(athleteId, competitionId)

        checkIfResultsCompleteOrAddMissingResults(category, athlete, competition, results)

        model["seriesId"] = seriesId
        model["competitionId"] = competitionId
        model["searchRequest"] = SearchRequest(seriesId = seriesId, competitionId = competitionId, term = athleteId.toString())
        model["athletes"] = ArrayList<AthleteDTO>()
        model["athlete"] = athleteRepository.getOneAthleteDTO(athleteId)
        model["resultContainer"] = ResultContainer(seriesId, competitionId, athlete.id, results)

        return RESULTS
    }

    @CheckOrganizationAccess
    @PostMapping("/sec/{organizationKey}/results")
    fun postResults(@AuthenticationPrincipal user: User, @PathVariable organizationKey: String, @RequestParam seriesId: Long,
                    @RequestParam competitionId: Long, resultContainer: ResultContainer, model: Model): String {
        val savedResults = ArrayList<Result>()
        var position = 0
        resultContainer.results.forEach {
            val result = resultRepository.getOne(it.id!!)
            result.result = it.result
            result.points = result.event!!.calculatePoints(result.result)
            result.position = position++

            val savedResult = resultRepository.save(result)
            savedResults.add(savedResult)
        }
        resultContainer.results = savedResults

        model["seriesId"] = seriesId
        model["competitionId"] = competitionId
        model["searchRequest"] = SearchRequest(resultContainer.seriesId, resultContainer.competitionId)
        model["athletes"] = ArrayList<AthleteDTO>()
        model["athlete"] = athleteRepository.getOneAthleteDTO(resultContainer.athleteId!!)
        model["resultContainer"] = resultContainer

        model["message"] = Message(Message.success, "Results saved!")

        return RESULTS
    }

    private fun checkIfResultsCompleteOrAddMissingResults(category: Category, athlete: Athlete, competition: Competition, results: MutableList<Result>) {
        if (category.events.size != results.size) {
            // TODO check if the results have the appropriate event type!
            val newResults = ArrayList<Result>()
            var i = 0
            category.events.forEach { event: Event ->
                if (!results.any { it.event == event }) {
                    val result = Result(category = category, event = event, athlete = athlete, competition = competition, position = i++)
                    resultRepository.save(result)
                    newResults.add(result)
                }
            }
            results.addAll(newResults)
        }
    }

}
