package ch.jtaf.control.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

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
