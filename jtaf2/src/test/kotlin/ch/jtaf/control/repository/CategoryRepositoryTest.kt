package ch.jtaf.control.repository

import ch.jtaf.AbstractBaseDataTest
import ch.jtaf.entity.Gender
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class CategoryRepositoryTest : AbstractBaseDataTest() {

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Test
    fun findByGenderAndYearFromLessThanEqualAndYearToGreaterThanEqual() {
        val category = categoryRepository.findByGenderAndYearFromLessThanEqualAndYearToGreaterThanEqual(
                Gender.MALE, 2004, 2004);

        assertEquals("A", category?.abbreviation)
    }

}
