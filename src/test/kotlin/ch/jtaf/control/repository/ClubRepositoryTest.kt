package ch.jtaf.control.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class ClubRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var clubRepository: ClubRepository

    @Test
    fun findByOrganizationIdOrderByAbbreviation() {
        val club = clubRepository.findByOrganizationIdOrderByAbbreviation(organizationId)

        assertEquals(1, club.size)
        assertEquals("TVT", club[0].abbreviation)
    }

}
