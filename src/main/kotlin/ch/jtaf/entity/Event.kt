package ch.jtaf.entity

import java.lang.Math.pow
import java.lang.Math.round
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

        var organizationId: Long? = null
) {

    fun calculatePoints(result: String): Int {
        val points = when (eventType) {
            EventType.RUN -> {
                a * pow((b - result.toDouble() * 100) / 100, c)
            }
            EventType.RUN_LONG -> {
                val parts = result.split("\\.".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val time = when {
                    parts.size == 1 -> (parts[0].toDouble() * 60)
                    parts.size == 2 -> (parts[0].toDouble() * 60) + parts[1].toDouble()
                    parts.size == 3 -> (parts[0].toDouble() * 60) + parts[1].toDouble() + (parts[1].toDouble() / 100)
                    else -> 0.0
                }
                a * pow((b - time * 100) / 100, c)
            }
            EventType.JUMP_THROW -> {
                a * pow(((result.toDouble() * 100) - b) / 100, c)
            }
        }
        return round(points).toInt()
    }
}