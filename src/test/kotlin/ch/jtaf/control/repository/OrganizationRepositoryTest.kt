package ch.jtaf.control.repository

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class OrganizationRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var organizationRepository: OrganizationRepository

    @Test
    fun findAll() {
        val list = organizationRepository.findAllByOwner(email)

        assertEquals(1, list.size)
        assertEquals("Concours Intersection", list[0].name)
    }

}
