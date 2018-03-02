package ch.jtaf.control.repository

import ch.jtaf.entity.Athlete
import ch.jtaf.entity.AthleteDTO
import ch.jtaf.entity.Competition
import ch.jtaf.entity.Result
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ResultRepository : JpaRepository<Result, Long> {

    fun findByAthleteIdAndCompetitionId(athleteId: Long, competitionId: Long): MutableList<Result>

}
