package ch.jtaf

import ch.jtaf.entity.*
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.EntityManager

abstract class AbstractBaseDataTest() {

    @Autowired
    lateinit var em: EntityManager

    @Before
    fun createTestData() {
        val series = Series(name = "CIS 2018", owner = "info@jtaf.ch")
        em.persist(series)

        val club = Club(abbreviation = "TVT", name = "TV Twann", owner = "info@jtaf.ch")
        em.persist(club)

        val athlete = Athlete(lastName = "Meier", firstName = "Max", yearOfBirth = 2004, club = club, owner = "info@jtaf.ch")
        em.persist(athlete)

        val event = Event(abbreviation = "80", name = "80 m", owner = "info@jtaf.ch")
        em.persist(event)

        val competition = Competition(name = "1. CIS Twann", seriesId = series.id)
        em.persist(competition)
        series.competitions.add(competition)

        val category = Category(abbreviation = "A", name = "Boys", yearFrom = 2000, yearTo = 2004, seriesId = series.id)
        em.persist(category)

        category.events.add(event)
        category.athletes.add(athlete)
    }
}