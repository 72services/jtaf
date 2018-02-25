package ch.jtaf.control.repository

import ch.jtaf.AbstractBaseDataTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class SeriesRepositoryTest : AbstractBaseDataTest() {

    @Autowired
    lateinit var seriesRepository: SeriesRepository

    @Test
    fun findAll() {
        val list = seriesRepository.findAllByOwner(email)

        assertEquals(1, list.size)
        assertEquals("CIS 2018", list[0].name)
    }

}
