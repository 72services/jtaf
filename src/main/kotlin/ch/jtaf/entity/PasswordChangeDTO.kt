package ch.jtaf.entity

data class PasswordChangeDTO(val userName: String,
                             var oldPassword: String = "",
                             var newPassword: String = "",
                             var newPasswordConfirm: String = "")
