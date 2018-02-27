package ch.jtaf.entity

import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
data class Organization(
        @Id @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,

        var key: String = "",
        var name: String = "",

        var owner: String? = null,

        @OneToMany(mappedBy = "organization")
        var athletes: MutableList<SecurityUser> = ArrayList()
)