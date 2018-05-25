package ch.jtaf.boundary.controller

data class NewUser(
        var email: String? = null,
        var password: String? = null,
        var passwordConfirmation: String? = null,
        var firstName: String? = null,
        var lastName: String? = null
)

