package ch.jtaf.entity

import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
data class Athlete(
        @Id @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,

        var lastName: String = "",
        var firstName: String = "",
        var yearOfBirth: Int = 0,
        @Enumerated(EnumType.STRING)
        var gender: Gender = Gender.MALE,

        var organizationId: Long? = null
) {
    @ManyToOne
    var club: Club? = null
}