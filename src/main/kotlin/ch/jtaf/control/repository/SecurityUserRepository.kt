package ch.jtaf.control.repository

import ch.jtaf.entity.SecurityUser
import org.springframework.data.jpa.repository.JpaRepository

interface SecurityUserRepository : JpaRepository<SecurityUser, Long> {

    fun findByEmail(email: String): SecurityUser?

}
