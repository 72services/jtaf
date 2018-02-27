package ch.jtaf.entity

import javax.persistence.*

@Entity
data class Series(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        var name: String = "",

        var locked: Boolean = false,
        var hidden: Boolean = false,

        @OneToMany
        @JoinColumn(name = "series_id", insertable = false, updatable = false)
        var competitions: MutableList<Competition> = ArrayList(),

        var organizationId: Long? = null
)