package ch.jtaf.control.repository

import ch.jtaf.AbstractBaseDataTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class SecurityUserRepositoryTest : AbstractBaseDataTest() {

    @Autowired
    lateinit var securityUserRepository: SecurityUserRepository

    @Test
    fun findByName() {
        val user = securityUserRepository.findByEmail("john.doe@jtaf.ch")

        assertNotNull(user)
        assertEquals("john.doe@jtaf.ch", user?.email)
    }

}
