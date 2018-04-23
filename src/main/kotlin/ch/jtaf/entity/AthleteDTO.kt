package ch.jtaf.entity

data class AthleteDTO(
        val id: Long = 0,
        val lastName: String = "",
        val firstName: String = "",
        val yearOfBirth: Int = 0,
        val gender: Gender = Gender.FEMALE,
        val club: String? = null,
        val category: String = ""
)