package ch.jtaf.entity

import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
data class Result(
        @Id @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,

        var result: String = "",
        var points: Int = 0,

        @ManyToOne
        var athlete: Athlete,

        @ManyToOne
        var category: Category,

        @ManyToOne
        var competition: Competition,

        var position: Int = 0
)