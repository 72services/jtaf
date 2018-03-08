package ch.jtaf.control.repository

import ch.jtaf.entity.Result
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ResultRepository : JpaRepository<Result, Long> {

    fun findByAthleteIdAndCompetitionId(athleteId: Long, competitionId: Long): MutableList<Result>

    fun findByCompetitionId(competitionId: Long): List<Result>

}
