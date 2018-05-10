package ch.jtaf.control.repository

import ch.jtaf.entity.Athlete
import ch.jtaf.entity.AthleteDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AthleteRepository : JpaRepository<Athlete, Long> {

    fun findByOrganizationIdOrderByLastNameAscFirstNameAsc(organizationId: Long): List<Athlete>

    @Query("SELECT a.* FROM athlete a WHERE a.organization_id = 1 AND a.id NOT IN (SELECT ca.athlete_id FROM category_athlete ca WHERE ca.athlete_id = a.id ORDER BY a.last_name, a.first_name)",
            nativeQuery = true)
    fun findByOrganizationIdAndNotAssignedToSeries(id: Long, seriesId: Long?): List<Athlete>

    @Query("SELECT NEW ch.jtaf.entity.AthleteDTO" +
            "(a.id, a.lastName, a.firstName, a.yearOfBirth, a.gender, cl.abbreviation, c.abbreviation) " +
            "FROM Category c JOIN c.athletes a LEFT JOIN a.club cl WHERE c.seriesId = :seriesId order by c.abbreviation, a.lastName, a.firstName")
    fun findAthleteDTOsBySeriesIdOrderByCategory(seriesId: Long): List<AthleteDTO>

    @Query("SELECT NEW ch.jtaf.entity.AthleteDTO" +
            "(a.id, a.lastName, a.firstName, a.yearOfBirth, a.gender, cl.abbreviation, c.abbreviation) " +
            "FROM Category c JOIN c.athletes a LEFT JOIN a.club cl WHERE c.seriesId = :seriesId order by cl.abbreviation, c.abbreviation, a.lastName, a.firstName")
    fun findAthleteDTOsBySeriesIdOrderByClub(seriesId: Long): List<AthleteDTO>

    @Query("SELECT COUNT(a) FROM Category c JOIN c.athletes a WHERE c.seriesId = :seriesId")
    fun getTotalNumberOfAthletesForSeries(seriesId: Long): Int?

    @Query("SELECT COUNT(DISTINCT r.athlete) FROM Result r WHERE r.competition.id = :competitionId GROUP BY r.competition")
    fun getTotalNumberOfAthleteWithResultsForCompetition(competitionId: Long): Int?

    @Query("SELECT NEW ch.jtaf.entity.AthleteDTO" +
            "(a.id, a.lastName, a.firstName, a.yearOfBirth, a.gender, cl.abbreviation, c.abbreviation) " +
            "FROM Category c JOIN c.athletes a LEFT JOIN a.club cl WHERE c.seriesId = :seriesId AND (LOWER(a.lastName) like LOWER(:name) OR LOWER(a.firstName) like LOWER(:name))")
    fun searchAthletes(seriesId: Long, name: String): List<AthleteDTO>

    @Query("SELECT NEW ch.jtaf.entity.AthleteDTO" +
            "(a.id, a.lastName, a.firstName, a.yearOfBirth, a.gender, cl.abbreviation, c.abbreviation) " +
            "FROM Category c JOIN c.athletes a LEFT JOIN a.club cl WHERE a.id = :athleteId")
    fun getOneAthleteDTO(athleteId: Long): AthleteDTO

}
