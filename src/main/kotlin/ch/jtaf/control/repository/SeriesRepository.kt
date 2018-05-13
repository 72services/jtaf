package ch.jtaf.control.repository

import ch.jtaf.entity.Series
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SeriesRepository : JpaRepository<Series, Long> {

    fun findByOrganizationId(organizationId: Long): List<Series>
}
