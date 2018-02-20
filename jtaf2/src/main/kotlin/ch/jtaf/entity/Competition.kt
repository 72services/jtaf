package ch.jtaf.entity

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Competition(
        @Id
        @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,

        var name: String = "",
        var competitionDate: LocalDate = LocalDate.MIN,
        var medalPercentage: Int = 0,
        var alwaysFirstThreeMedals: Boolean = true,
        var locked: Boolean = false,

        @ManyToOne
        var series: Series? = null
)