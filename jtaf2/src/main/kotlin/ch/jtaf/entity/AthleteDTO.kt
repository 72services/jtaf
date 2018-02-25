package ch.jtaf.entity

data class AthleteDTO(
        var id: Long = 0,
        var lastName: String = "",
        var firstName: String = "",
        var yearOfBirth: Int = 0,
        var gender: Gender = Gender.MALE,
        var club: String = "",
        var category: String = ""
)