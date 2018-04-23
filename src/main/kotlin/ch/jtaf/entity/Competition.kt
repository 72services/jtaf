package ch.jtaf.entity

import java.sql.Date
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
data class Competition(
        @Id
        @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,

        var name: String = "",
        var competitionDate: Date = Date(System.currentTimeMillis()),
        var medalPercentage: Int = 0,
        var alwaysFirstThreeMedals: Boolean = true,
        var locked: Boolean = false,

        @Column(name = "series_id")
        var seriesId: Long? = null
) {
    @Transient
    var numberOfAthletes: Int = 0

    @Transient
    var numberOfAthletesWithResults: Int = 0
}