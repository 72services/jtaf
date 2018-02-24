package ch.jtaf.control.repository

import ch.jtaf.entity.Athlete
import ch.jtaf.entity.Category
import ch.jtaf.entity.Club
import ch.jtaf.entity.Series
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClubRepository : JpaRepository<Club, Long> {

    fun findAllByOwner(username: String): List<Club>
}
