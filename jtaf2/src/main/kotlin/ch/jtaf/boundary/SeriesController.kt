package ch.jtaf.boundary

import ch.jtaf.control.repository.SeriesRepository
import ch.jtaf.entity.Series
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/sec/series")
class SeriesController(private val seriesRepository: SeriesRepository) {

    @GetMapping()
    fun get(): ModelAndView {
        val mav = ModelAndView("/sec/series")
        mav.model["message"] = ""
        mav.model["series"] = Series()
        return mav
    }

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/series")
        mav.model["message"] = ""

        val series = seriesRepository.findById(id)
        if (series.isPresent) {
            mav.model["series"] = series.get()
        } else {
            throw IllegalStateException()
        }

        return mav
    }


    @PostMapping
    fun post(@AuthenticationPrincipal user: User, series: Series): ModelAndView {
        series.owner = user.username
        seriesRepository.save(series)

        val mav = ModelAndView()
        mav.model["message"] = "Series saved!"
        return mav
    }
}
