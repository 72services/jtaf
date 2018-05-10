package ch.jtaf.control.repository

import ch.jtaf.entity.Result
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ResultRepository : JpaRepository<Result, Long> {

    fun findByAthleteIdAndCompetitionIdOrderByPosition(athleteId: Long, competitionId: Long): MutableList<Result>

    fun findByCompetitionId(competitionId: Long): List<Result>

    fun findByCompetitionSeriesId(seriesId: Long): List<Result>

    @Modifying
    @Query("delete from Result r where r.id in (select r.id from Result r where r.competition.competitionDate <= current_date and r.athlete.id = :athleteId)")
    fun deleteResultsFromActiveCompetitions(athleteId: Long)

    @Modifying
    @Query("delete from Result r where r.id in (select r.id from Result r where r.category.id = :categoryId and r.athlete.id = :athleteId)")
    fun deleteResultsByCategoryIdAndAthleteId(categoryId: Long?, athleteId: Long)
}
