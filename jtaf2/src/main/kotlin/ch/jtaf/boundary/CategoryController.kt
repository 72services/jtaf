package ch.jtaf.boundary

import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.entity.Category
import ch.jtaf.entity.Competition
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/sec/category")
class CategoryController(private val categoryRepository: CategoryRepository,
                         private val seriesAuthorizationChecker: SeriesAuthorizationChecker) {

    @GetMapping()
    fun get(@RequestParam("seriesId") seriesId: Long): ModelAndView {
        seriesAuthorizationChecker.checkIfUserAccessToSeries(seriesId)

        val mav = ModelAndView("/sec/category")
        mav.model["message"] = ""

        val category = Category()
        category.seriesId = seriesId
        mav.model["category"] = category

        return mav
    }

    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/category")
        mav.model["message"] = ""

        val category = categoryRepository.findById(id)
        if (category.isPresent) {
            mav.model["category"] = category.get()
        } else {
            throw IllegalStateException("Category not found")
        }

        return mav
    }

    @PostMapping
    fun post(category: Category): ModelAndView {
        seriesAuthorizationChecker.checkIfUserAccessToSeries(category.seriesId)

        categoryRepository.save(category)

        val mav = ModelAndView()
        mav.model["message"] = "Category saved!"
        return mav
    }
}
