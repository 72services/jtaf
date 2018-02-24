package ch.jtaf.boundary

import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.SeriesRepository
import ch.jtaf.entity.Series
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/sec/series")
class SeriesController(private val seriesRepository: SeriesRepository,
                       private val categoryRepository: CategoryRepository,
                       private val seriesAuthorizationChecker: SeriesAuthorizationChecker) {

    @GetMapping()
    fun get(): ModelAndView {
        val mav = ModelAndView()
        mav.model["message"] = ""
        mav.model["series"] = Series()
        return mav
    }

    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: Long): ModelAndView {
        val series = seriesAuthorizationChecker.checkIfUserAccessToSeries(id)

        val mav = ModelAndView("/sec/series")
        mav.model["message"] = ""
        mav.model["series"] = series
        mav.model["categories"] = categoryRepository.findAllBySeriesId(id)

        return mav
    }

    @GetMapping("{id}/delete")
    fun deleteById(@PathVariable("id") id: Long): ModelAndView {
        seriesAuthorizationChecker.checkIfUserAccessToSeries(id)

        seriesRepository.deleteById(id)

        val mav = ModelAndView("/sec/serieslist")
        mav.model["seriesList"] = seriesRepository.findAll()
        return mav
    }

    @PostMapping
    fun post(@AuthenticationPrincipal user: User, series: Series): ModelAndView {
        if (series.id != null) {
            seriesAuthorizationChecker.checkIfUserAccessToSeries(series.id)
        }

        series.owner = user.username
        seriesRepository.save(series)

        val mav = ModelAndView()
        mav.model["message"] = "Series saved!"
        return mav
    }
}
