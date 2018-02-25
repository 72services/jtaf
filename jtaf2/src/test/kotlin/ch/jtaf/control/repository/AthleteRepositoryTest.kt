package ch.jtaf.control.repository

import ch.jtaf.AbstractBaseDataTest
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class AthleteRepositoryTest : AbstractBaseDataTest() {

    @Autowired
    lateinit var athleteRepository: AthleteRepository

    @Test
    fun findAthleteDTOsBySeriesId() {
        athleteRepository.findAthleteDTOsBySeriesId(1);
    }

}
