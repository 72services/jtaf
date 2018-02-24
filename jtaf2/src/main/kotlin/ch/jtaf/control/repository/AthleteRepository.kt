package ch.jtaf.control.repository

import ch.jtaf.entity.Athlete
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AthleteRepository : JpaRepository<Athlete, Long> {

    fun findAllByOwner(username: String): List<Athlete>
}
