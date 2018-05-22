package ch.jtaf.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Result(
        @Id @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,

        var result: String = "",
        var points: Int = 0,

        @ManyToOne(optional = false)
        var category: Category? = null,

        @ManyToOne(optional = false)
        var event: Event? = null,

        @ManyToOne(optional = false)
        var athlete: Athlete? = null,

        @ManyToOne(optional = false)
        var competition: Competition? = null,

        var position: Int = 0
) {
    fun toInt(): Int {
        return result.replace("\\.".toRegex(), "").toInt()
    }
}