package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.CATEGORY
import ch.jtaf.boundary.dto.Message
import ch.jtaf.boundary.security.CheckOrganizationAccess
import ch.jtaf.boundary.web.HttpContentProducer
import ch.jtaf.control.repository.CategoryRepository
import ch.jtaf.control.repository.EventRepository
import ch.jtaf.control.service.SheetService
import ch.jtaf.entity.Category
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import javax.persistence.EntityManager

@Controller
class CategoryController(private val categoryRepository: CategoryRepository,
                         private val eventRepository: EventRepository,
                         private val sheetService: SheetService,
                         private val entityManager: EntityManager) {

    val httpContentUtil = HttpContentProducer()

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/series/{seriesId}/category")
    fun get(@PathVariable organizationKey: String, @PathVariable seriesId: Long,
            model: Model): String {
        val category = Category()
        category.seriesId = seriesId

        model["seriesId"] = seriesId
        model["category"] = category

        return CATEGORY
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/series/{seriesId}/category/{categoryId}")
    fun getById(@PathVariable("organizationKey") organizationKey: String, @PathVariable("seriesId") seriesId: Long,
                @PathVariable("categoryId") categoryId: Long, model: Model): String {

        model["seriesId"] = seriesId
        model["category"] = categoryRepository.getOne(categoryId)

        return CATEGORY
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/series/{seriesId}/category/{categoryId}/event/{eventId}")
    fun addEvent(@PathVariable organizationKey: String, @PathVariable categoryId: Long,
                 @PathVariable seriesId: Long, @PathVariable eventId: Long, model: Model): String {
        val category = categoryRepository.getOne(categoryId)
        val event = eventRepository.getOne(eventId)
        category.events.add(event)

        categoryRepository.save(category)

        model["seriesId"] = seriesId
        model["category"] = category

        return CATEGORY
    }

    @CheckOrganizationAccess
    @Transactional
    @GetMapping("/sec/{organizationKey}/series/{seriesId}/category/{categoryId}/event/{eventId}/up")
    fun eventMoveUp(@PathVariable organizationKey: String, @PathVariable categoryId: Long, @PathVariable seriesId: Long,
                    @PathVariable eventId: Long, model: Model): String {
        return moveEvent(categoryId, seriesId, eventId, true, model)
    }

    @CheckOrganizationAccess
    @Transactional
    @GetMapping("/sec/{organizationKey}/series/{seriesId}/category/{categoryId}/event/{eventId}/down")
    fun eventMoveDown(@PathVariable organizationKey: String, @PathVariable categoryId: Long,
                      @PathVariable seriesId: Long, @PathVariable eventId: Long, model: Model): String {
        return moveEvent(categoryId, seriesId, eventId, false, model)
    }

    private fun moveEvent(id: Long, seriesId: Long, eventId: Long, up: Boolean, model: Model): String {
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
        entityManager.flush()

        category.events = events
        categoryRepository.save(category)

        model["seriesId"] = seriesId
        model["category"] = category

        return CATEGORY
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/series/{seriesId}/category/{categoryId}/event/{eventId}/delete")
    fun deleteById(@PathVariable organizationKey: String, @PathVariable categoryId: Long, @PathVariable seriesId: Long,
                   @PathVariable eventId: Long, model: Model): String {
        val category = categoryRepository.getOne(categoryId)
        val event = eventRepository.getOne(eventId)
        category.events.remove(event)

        categoryRepository.save(category)

        model["seriesId"] = seriesId
        model["category"] = category

        return CATEGORY
    }

    @CheckOrganizationAccess
    @PostMapping("/sec/{organizationKey}/series/{seriesId}/category")
    fun post(@PathVariable organizationKey: String, @PathVariable seriesId: Long, category: Category, model: Model): String {
        model["seriesId"] = seriesId

        if (category.id == null) {
            categoryRepository.save(category)

            model["category"] = category
        } else {
            val categoryFromDb = categoryRepository.getOne(category.id!!)
            categoryFromDb.abbreviation = category.abbreviation
            categoryFromDb.name = category.name
            categoryFromDb.yearFrom = category.yearFrom
            categoryFromDb.yearTo = category.yearTo
            categoryFromDb.gender = category.gender
            categoryRepository.save(categoryFromDb)

            model["category"] = categoryFromDb
        }

        model["message"] = Message(Message.success, "Category saved!")

        return CATEGORY
    }

    @CheckOrganizationAccess
    @GetMapping("/sec/{organizationKey}/series/{seriesId}/category/{categoryId}/sheet")
    fun getSheet(@PathVariable organizationKey: String, @PathVariable seriesId: Long, @PathVariable categoryId: Long):
            ResponseEntity<ByteArray> {

        val emptySheet = sheetService.createEmptySheet(seriesId, categoryId)
        return httpContentUtil.getContentAsPdf("sheet_category_$categoryId.pdf", emptySheet)
    }

}
