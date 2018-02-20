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
        var c: Double = 0.0
)