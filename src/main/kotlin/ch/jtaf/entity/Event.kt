package ch.jtaf.entity

import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
data class Event(
        @Id @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,

        var abbreviation: String = "",
        var name: String = "",
        @Enumerated(EnumType.STRING)
        var eventType: EventType = EventType.RUN,
        @Enumerated(EnumType.STRING)
        var gender: Gender = Gender.MALE,

        var a: Double = 0.0,
        var b: Double = 0.0,
        var c: Double = 0.0,

        var position: Int = 0,

        var organizationId: Long? = null
) {

    fun calculatePoints(result: String): Long {
        var points = 0.0
        if (result.toDouble() > 0) {
            when (eventType) {
                EventType.RUN -> {
                    points = a * Math.pow((b - result.toDouble() * 100) / 100, c)
                }
                EventType.RUN_LONG -> {
                    val parts = result.split("\\.".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    var time = 0.0
                    time = if (parts.size == 1) {
                        parts[0].toDouble() * 60
                    } else {
                        parts[0].toDouble() * 60 + parts[1].toDouble()
                    }
                    points = a * Math.pow((b - time * 100) / 100, c)
                }
                EventType.JUMP_THROW -> {
                    points = a * Math.pow((result.toDouble() * 100 - b) / 100, c)
                }
            }
        }
        return Math.round(points)
    }
}