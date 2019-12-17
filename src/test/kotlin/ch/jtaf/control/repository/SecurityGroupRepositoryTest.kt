package ch.jtaf.control.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class SecurityGroupRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var securityGroupRepository: SecurityGroupRepository

    @Test
    fun findByName() {
        val group = securityGroupRepository.findByName("ADMIN")

        assertNotNull(group)
        assertEquals("ADMIN", group?.name)
    }

}
