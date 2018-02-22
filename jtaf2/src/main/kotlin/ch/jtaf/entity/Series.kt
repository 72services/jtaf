package ch.jtaf.entity

import javax.persistence.*

@Entity
data class Series(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        var name: String = "",

        var locked: Boolean = false,
        var hidden: Boolean = false,

        @OneToMany(mappedBy = "series")
        var competitions: MutableList<Competition> = ArrayList(),

        var owner: String? = null
)