package ch.jtaf.boundary.controller

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

data class NewUser(
        @NotNull @Email
        var email: String? = null,
        @NotNull
        var password: String? = null,
        @NotNull
        var passwordConfirmation: String? = null,
        @NotNull
        var firstName: String? = null,
        @NotNull
        var lastName: String? = null
)

