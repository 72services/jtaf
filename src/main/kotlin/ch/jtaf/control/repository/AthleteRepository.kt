package ch.jtaf.control.repository

import ch.jtaf.entity.Athlete
import ch.jtaf.entity.AthleteDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AthleteRepository : JpaRepository<Athlete, Long> {

    fun findByOrganizationId(organizationId: Long): List<Athlete>

    @Query("SELECT a.* FROM athlete a WHERE a.organization_id = 1 AND a.id NOT IN (SELECT ca.athlete_id FROM category_athlete ca WHERE ca.athlete_id = a.id)",
            nativeQuery = true)
    fun findByOrganizationIdAndNotAssignedToSeries(id: Long, seriesId: Long?): List<Athlete>

    @Query("SELECT NEW ch.jtaf.entity.AthleteDTO" +
            "(a.id, a.lastName, a.firstName, a.yearOfBirth, a.gender, cl.abbreviation, c.abbreviation) " +
            "FROM Category c JOIN c.athletes a LEFT JOIN a.club cl WHERE c.seriesId = ?1")
    fun findAthleteDTOsBySeriesId(id: Long): List<AthleteDTO>

    @Query("SELECT COUNT(a) FROM Category c JOIN c.athletes a WHERE c.seriesId = ?1")
    fun getTotalNumberOfAthletesForSeries(id: Long): Int?

    @Query("SELECT COUNT(DISTINCT r.athlete) FROM Result r WHERE r.competition.id = ?1 GROUP BY r.competition")
    fun getTotalNumberOfAthleteWithResultsForCompetition(id: Long): Int?

    @Query("SELECT NEW ch.jtaf.entity.AthleteDTO" +
            "(a.id, a.lastName, a.firstName, a.yearOfBirth, a.gender, cl.abbreviation, c.abbreviation) " +
            "FROM Category c JOIN c.athletes a LEFT JOIN a.club cl WHERE c.seriesId = ?1 AND (UPPER(a.lastName) like UPPER(?2) OR UPPER(a.firstName) like UPPER(?2))")
    fun searchAthletes(seriesId: Long, name: String): List<AthleteDTO>

    @Query("SELECT NEW ch.jtaf.entity.AthleteDTO" +
            "(a.id, a.lastName, a.firstName, a.yearOfBirth, a.gender, cl.abbreviation, c.abbreviation) " +
            "FROM Category c JOIN c.athletes a LEFT JOIN a.club cl WHERE a.id = ?1")
    fun getOneAthleteDTO(athleteId: Long): AthleteDTO

}
