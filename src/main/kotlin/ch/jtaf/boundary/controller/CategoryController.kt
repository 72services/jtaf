package ch.jtaf.boundary.controller

import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.EventRepository
import ch.jtaf.entity.Category
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class CategoryController(private val categoryRepository: CategoryRepository,
                         private val eventRepository: EventRepository,
                         private val organizationAuthorizationChecker: OrganizationAuthorizationChecker) {

    @GetMapping("/sec/{organization}/series/{seriesId}/category")
    fun get(@PathVariable("organization") organizationKey: String,
            @PathVariable("seriesId") seriesId: Long): ModelAndView {
        val category = Category()
        category.seriesId = seriesId

        val mav = ModelAndView("/sec/category")
        mav.model["seriesId"] = seriesId
        mav.model["category"] = category

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/sec/{organization}/series/{seriesId}/category/{id}")
    fun getById(@PathVariable("organization") organizationKey: String,
                @PathVariable("seriesId") seriesId: Long,
                @PathVariable("id") id: Long): ModelAndView {
        val mav = ModelAndView("/sec/category")
        mav.model["seriesId"] = seriesId
        mav.model["category"] = categoryRepository.getOne(id)

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/sec/{organization}/series/{seriesId}/category/{id}/event/{eventId}")
    fun addEvent(@PathVariable("organization") organizationKey: String,
                 @PathVariable("id") id: Long,
                 @PathVariable("seriesId") seriesId: Long,
                 @PathVariable("eventId") eventId: Long): ModelAndView {
        val category = categoryRepository.getOne(id)
        val event = eventRepository.getOne(eventId)
        category.events.add(event)

        categoryRepository.save(category)

        val mav = ModelAndView("/sec/category")
        mav.model["seriesId"] = seriesId
        mav.model["category"] = category

        mav.model["message"] = null
        return mav
    }

    @GetMapping("/sec/{organization}/series/{seriesId}/category/{id}/event/{eventId}/up")
    fun eventMoveUp(@PathVariable("organization") organizationKey: String,
                    @PathVariable("id") id: Long,
                    @PathVariable("seriesId") seriesId: Long,
                    @PathVariable("eventId") eventId: Long): ModelAndView {
        return moveEvent(id, seriesId, eventId, true)
    }

    @GetMapping("/sec/{organization}/series/{seriesId}/category/{id}/event/{eventId}/down")
    fun eventMoveDown(@PathVariable("organization") organizationKey: String,
                      @PathVariable("id") id: Long,
                      @PathVariable("seriesId") seriesId: Long,
                      @PathVariable("eventId") eventId: Long): ModelAndView {
        return moveEvent(id, seriesId, eventId, false)
    }

    private fun moveEvent(id: Long, seriesId: Long, eventId: Long, up: Boolean): ModelAndView {
        val category = categoryRepository.getOne(id)
        val event = eventRepository.getOne(eventId)

        val events = category.events
        val index = events.indexOf(event)
        if (up) {
            if (index > 0) {
                events.remove(event)
                events.add(index - 1, event)
            }
        } else {
            if (index < category.events.size - 1) {
                events.remove(event)
                events.add(index + 1, event)
            }
        }

        category.events = ArrayList()
        categoryRepository.save(category)

        category.events = events
        categoryRepository.save(category)

        val mav = ModelAndView("/sec/category")
        mav.model["seriesId"] = seriesId
        mav.model["category"] = category

        mav.model["message"] = null
        return mav
    }


    @GetMapping("/sec/{organization}/series/{seriesId}/category/{id}/event/{eventId}/delete")
    fun deleteById(@PathVariable("organization") organizationKey: String,
                   @PathVariable("id") id: Long,
                   @PathVariable("seriesId") seriesId: Long,
                   @PathVariable("eventId") eventId: Long): ModelAndView {
        val category = categoryRepository.getOne(id)
        val event = eventRepository.getOne(eventId)
        category.events.remove(event)

        categoryRepository.save(category)

        val mav = ModelAndView("/sec/category")
        mav.model["seriesId"] = seriesId
        mav.model["category"] = category

        mav.model["message"] = null
        return mav
    }

    @PostMapping("/sec/{organization}/series/{seriesId}/category/")
    fun post(@PathVariable("organization") organizationKey: String,
             @PathVariable("seriesId") seriesId: Long,
             category: Category): ModelAndView {
        val mav = ModelAndView("/sec/category")
        mav.model["seriesId"] = seriesId

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

        mav.model["message"] = Message(Message.success, "Category saved!")
        return mav
    }
}
