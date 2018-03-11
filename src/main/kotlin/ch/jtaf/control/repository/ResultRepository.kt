package ch.jtaf.control.repository

import ch.jtaf.entity.Result
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ResultRepository : JpaRepository<Result, Long> {

    fun findByAthleteIdAndCompetitionId(athleteId: Long, competitionId: Long): MutableList<Result>

    fun findByCompetitionId(competitionId: Long): List<Result>

    fun findByCompetitionSeriesId(seriesId: Long): List<Result>

    @Modifying
    @Query("delete from Result r where r.id in (select r.id from Result r where r.competition.competitionDate <= current_date and r.athlete.id = ?1)")
    fun deleteResultsFromActiveCompetitions(id: Long)

    @Modifying
    @Query("delete from Result r where r.id in (select r.id from Result r where r.category.id =?1 and r.athlete.id = ?2)")
    fun deleteResultsByCategoryIdAndAthleteId(id: Long?, athleteId: Long)

}
