package ch.jtaf.control.repository

import ch.jtaf.entity.Athlete
import ch.jtaf.entity.AthleteDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AthleteRepository : JpaRepository<Athlete, Long> {

    fun findAllByOwner(username: String): List<Athlete>

    @Query("SELECT NEW ch.jtaf.entity.AthleteDTO(" +
            "a.id, a.lastName, a.firstName, a.yearOfBirth, a.gender, a.club.abbreviation, c.abbreviation" +
            ") FROM Category c JOIN c.athletes a WHERE c.seriesId = ?1")
    fun findAthleteDTOsBySeriesId(id: Long): List<AthleteDTO>
}
