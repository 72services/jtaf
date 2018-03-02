package ch.jtaf.boundary

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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class ResultsController(private val athleteRepository: AthleteRepository,
                        private val competitionRepository: CompetitionRepository,
                        private val resultRepository: ResultRepository,
                        private val categoryRepository: CategoryRepository) {

    @GetMapping("/sec/{organization}/athlete/{athleteId}/results}")
    fun getByAthlete(@AuthenticationPrincipal user: User,
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
        mav.model["message"] = ""
        mav.model["seriesId"] = seriesId
        mav.model["competitionId"] = competitionId
        mav.model["athlete"] = athlete
        mav.model["results"] = results
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
