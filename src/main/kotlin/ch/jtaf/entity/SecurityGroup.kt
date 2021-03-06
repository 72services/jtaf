package ch.jtaf.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id

@Entity
data class SecurityGroup(
        @Id @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,

        var name: String = ""
)