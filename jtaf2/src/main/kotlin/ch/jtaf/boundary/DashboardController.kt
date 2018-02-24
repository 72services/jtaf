package ch.jtaf.boundary

import ch.jtaf.control.repository.SeriesRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/dashboard")
class DashboardController(private val seriesRepository: SeriesRepository) {

    @GetMapping
    fun get(): ModelAndView {
        val mav = ModelAndView()
        mav.model["seriesList"] = seriesRepository.findAll()
        return mav
    }
}
