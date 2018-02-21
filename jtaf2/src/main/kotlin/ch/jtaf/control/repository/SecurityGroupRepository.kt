package ch.jtaf.control.repository

import ch.jtaf.entity.SecurityGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SecurityGroupRepository : JpaRepository<SecurityGroup, Long> {

    fun findByName(s: String): SecurityGroup?
}