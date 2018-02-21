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
        var athlete: Athlete? = null,

        @ManyToOne
        var category: Category? = null,

        @ManyToOne
        var competition: Competition? = null,

        var position: Int = 0
)