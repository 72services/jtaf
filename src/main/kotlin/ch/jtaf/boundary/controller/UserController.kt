package ch.jtaf.boundary.controller

import ch.jtaf.boundary.dto.Message
import ch.jtaf.control.repository.SecurityUserRepository
import ch.jtaf.entity.PasswordChangeDTO
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/sec/user")
class UserController(private val securityUserRepository: SecurityUserRepository,
                     private val passwordEncoder: PasswordEncoder) {

    @GetMapping
    fun get(@AuthenticationPrincipal user: User, @RequestParam organizationKey: String?): ModelAndView {
        val mav = ModelAndView("sec/user")
        mav.model["organizationKey"] = organizationKey
        mav.model["user"] = user
        mav.model["passwordChangeDTO"] = PasswordChangeDTO(userName = user.username)
        return mav
    }

    @PostMapping
    fun changePassword(@AuthenticationPrincipal user: User,
                       passwordChangeDTO: PasswordChangeDTO,
                       @RequestParam organizationKey: String?): ModelAndView {
        val mav = ModelAndView("sec/user")
        mav.model["organizationKey"] = organizationKey
        mav.model["user"] = user

        val securityUser = securityUserRepository.findByEmail(passwordChangeDTO.userName)
        when {
            !passwordEncoder.matches(passwordChangeDTO.oldPassword, securityUser?.secret) -> {
                mav.model["message"] = Message(Message.danger, "Old password incorrect!")
            }
            passwordChangeDTO.newPassword != passwordChangeDTO.newPasswordConfirm -> {
                mav.model["message"] = Message(Message.danger, "Password confirm does not match!")
            }
            else -> {
                securityUser?.secret = passwordEncoder.encode(passwordChangeDTO.newPassword)
                securityUserRepository.save(securityUser!!)
                mav.model["message"] = Message(Message.success, "Password changed!")
            }
        }
        return mav
    }
}
