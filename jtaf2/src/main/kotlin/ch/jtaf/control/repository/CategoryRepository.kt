package ch.jtaf.control.repository

import ch.jtaf.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {

    fun findAllBySeriesId(seriesId: Long): List<Category>
}
