package ch.jtaf.control.repository

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class ClubRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var clubRepository: ClubRepository

    @Test
    fun findAllByOwner() {
        val club = clubRepository.findByOrganizationId(organizationId)

        assertEquals(1, club.size)
        assertEquals("TVT", club[0].abbreviation)
    }

}