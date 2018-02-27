package ch.jtaf.boundary

import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.EventRepository
import ch.jtaf.entity.Category
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
class CategoryController(private val categoryRepository: CategoryRepository,
                         private val eventRepository: EventRepository,
                         private val organizationAuthorizationChecker: OrganizationAuthorizationChecker) {

    @GetMapping("/sec/{organization}/category")
    fun get(@PathVariable("organization") organization: String,
            @RequestParam("seriesId") seriesId: Long): ModelAndView {
        val mav = ModelAndView("/sec/category")
        mav.model["message"] = ""

        val category = Category()
        category.seriesId = seriesId
        mav.model["category"] = category

        return mav
    }

    @GetMapping("/sec/{organization}/category/{id}/event/{eventId}")
    fun addEvent(@PathVariable("organization") organization: String,
                 @PathVariable("id") id: Long,
                 @PathVariable("eventId") eventId: Long): ModelAndView {
        val mav = ModelAndView("/sec/category")
        mav.model["message"] = ""

        val category = categoryRepository.getOne(id)
        val event = eventRepository.getOne(eventId)
        category.events.add(event)
        categoryRepository.save(category)

        mav.model["category"] = category

        return mav
    }

    @GetMapping("/sec/{organization}/category/{id}/event/{eventId}/delete")
    fun deleteById(@PathVariable("organization") organization: String,
                   @PathVariable("id") id: Long,
                   @PathVariable("eventId") eventId: Long): ModelAndView {
        val mav = ModelAndView("/sec/category")
        mav.model["message"] = ""

        val category = categoryRepository.getOne(id)
        val event = eventRepository.getOne(eventId)
        category.events.remove(event)

        categoryRepository.save(category)

        mav.model["category"] = category

        return mav
    }

    @GetMapping("/sec/{organization}/category/{id}")
    fun getById(@PathVariable("organization") organization: String,
                @PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/category")
        mav.model["message"] = ""

        mav.model["category"] = categoryRepository.getOne(id)

        return mav
    }

    @PostMapping("/sec/{organization}/category/")
    fun post(@PathVariable("organization") organization: String,
             category: Category): ModelAndView {
        val mav = ModelAndView("/sec/category")

        if (category.id == null) {
            categoryRepository.save(category)
            mav.model["category"] = category
        } else {
            val categoryFromDb = categoryRepository.getOne(category.id!!)
            categoryFromDb.abbreviation = category.abbreviation
            categoryFromDb.name = category.name
            categoryFromDb.yearFrom = category.yearFrom
            categoryFromDb.yearTo = category.yearTo
            categoryFromDb.gender = category.gender
            categoryRepository.save(categoryFromDb)

            mav.model["category"] = categoryFromDb
        }

        mav.model["message"] = "Category saved!"
        return mav
    }
}
