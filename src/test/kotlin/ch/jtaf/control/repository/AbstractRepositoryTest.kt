package ch.jtaf.control.repository

import ch.jtaf.entity.*
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.EntityManager

abstract class AbstractRepositoryTest {

    val email = "john.doe@jtaf.ch"
    var organizationId = 0L
    var seriesId = 0L
    var competitionId = 0L
    var athleteId = 0L

    @Autowired
    private lateinit var em: EntityManager

    @Before
    fun createTestData() {
        val securityGroup = SecurityGroup(name = "ADMIN")
        em.persist(securityGroup)

        val securityUser = SecurityUser(email = email)
        securityUser.groups.add(securityGroup)
        em.persist(securityUser)

        val organization = Organization(key = "cis", name ="Concours Intersection", owner = email)
        em.persist(organization)
        organizationId = organization.id!!

        val series = Series(name = "CIS 2018", organizationId = organizationId)
        em.persist(series)
        seriesId = series.id!!

        val club = Club(abbreviation = "TVT", name = "TV Twann", organizationId = organizationId)
        em.persist(club)

        val athlete = Athlete(lastName = "Meier", firstName = "Max", yearOfBirth = 2004, organizationId = organizationId)
        athlete.club = club
        em.persist(athlete)
        athleteId = athlete.id!!

        val event = Event(abbreviation = "80", name = "80 m", organizationId = organizationId)
        em.persist(event)

        val competition = Competition(name = "1. CIS Twann", seriesId = series.id)
        em.persist(competition)
        competitionId = competition.id!!

        series.competitions.add(competition)

        val category = Category(abbreviation = "A", name = "Boys", yearFrom = 2000, yearTo = 2004, seriesId = series.id)
        em.persist(category)

        category.events.add(event)
        category.athletes.add(athlete)
    }
}