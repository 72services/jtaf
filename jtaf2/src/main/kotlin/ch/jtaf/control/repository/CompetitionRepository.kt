package ch.jtaf.control.repository

import ch.jtaf.entity.Competition
import ch.jtaf.entity.Series
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompetitionRepository : JpaRepository<Competition, Long> {

    fun findAllBySeriesId(seriesId: Long): List<Competition>
}
