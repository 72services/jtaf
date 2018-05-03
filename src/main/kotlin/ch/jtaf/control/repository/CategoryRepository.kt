package ch.jtaf.control.repository

import ch.jtaf.entity.Category
import ch.jtaf.entity.Gender
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long> {

    fun findAllBySeriesIdOrderByAbbreviation(seriesId: Long): List<Category>

    fun findBySeriesIdAndGenderAndYearFromLessThanEqualAndYearToGreaterThanEqual(
            seriesId: Long, gender: Gender, dateFrom: Int, dateTo: Int): Category?

    fun findByAthletesId(id: Long): List<Category>
}
