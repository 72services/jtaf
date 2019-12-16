package ch.jtaf.control.repository

import ch.jtaf.entity.Gender
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class CategoryRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Test
    fun findByGenderAndYearFromLessThanEqualAndYearToGreaterThanEqual() {
        val category = categoryRepository.findBySeriesIdAndGenderAndYearFromLessThanEqualAndYearToGreaterThanEqual(
                seriesId, Gender.MALE, 2004, 2004)

        assertNotNull(category)
        assertEquals("A", category?.abbreviation)
    }

}
