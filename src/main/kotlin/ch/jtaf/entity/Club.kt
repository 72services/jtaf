package ch.jtaf.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id

@Entity
data class Club(
        @Id @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,

        var abbreviation: String = "",
        var name: String = "",

        var organizationId: Long? = null
)