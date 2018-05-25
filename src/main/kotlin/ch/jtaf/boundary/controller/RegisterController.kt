package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.DASHBOARD
import ch.jtaf.boundary.controller.Views.REGISTER
import ch.jtaf.boundary.controller.Views.REGISTER_CONFIRMATION
import ch.jtaf.boundary.controller.Views.USER
import ch.jtaf.boundary.dto.Message
import ch.jtaf.control.repository.SecurityUserRepository
import ch.jtaf.control.service.MailService
import ch.jtaf.entity.PasswordChangeDTO
import ch.jtaf.entity.SecurityUser
import com.sendgrid.SendGrid
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@Controller
@RequestMapping("/register")
class RegisterController(private val securityUserRepository: SecurityUserRepository,
                         private val passwordEncoder: PasswordEncoder,
                         private val mailService: MailService) {

    @GetMapping
    fun get(model: Model): String {
        model["newUser"] = NewUser()

        return REGISTER
    }

    @PostMapping
    fun changePassword(newUser: NewUser, model: Model): String {
        model["newUser"] = newUser

        if (newUser.password != newUser.passwordConfirmation) {
            model["message"] = Message(Message.danger, "Password confirm does not match!")
        } else {
            var securityUser = SecurityUser(email = newUser.email!!, secret = passwordEncoder.encode(newUser.password), firstName = newUser.firstName
                    ?: "", lastName = newUser.lastName ?: "", confirmationId = UUID.randomUUID().toString())
            //securityUserRepository.save(securityUser)

            mailService.sendText(MailService.DEFAULT_FROM, newUser.email!!, "Your Registration on jtaf.io",
                    "Please confirm your registration by clicking on the link " +
                            "http://localhost:5000/register/confirm?confirmationId=${securityUser.confirmationId}")

            model["confirmation"] = true
            model["message"] = Message(Message.success, "Thank you for your registration. You will receive a confirmation mail")
        }

        return REGISTER
    }

    @GetMapping("/confirm")
    fun confirm(@RequestParam confirmationId: String): String {

        return REGISTER_CONFIRMATION
    }
}
