package ch.jtaf.control.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class SecurityUserRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var securityUserRepository: SecurityUserRepository

    @Test
    fun findByName() {
        val user = securityUserRepository.findByEmail("john.doe@jtaf.ch")

        assertNotNull(user)
        assertEquals("john.doe@jtaf.ch", user?.email)
    }

}
