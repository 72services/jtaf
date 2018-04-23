package ch.jtaf.control.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
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
