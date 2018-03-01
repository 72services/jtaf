package ch.jtaf.boundary

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/sec/user")
class UserController {

    @GetMapping
    fun get(@AuthenticationPrincipal user: User): ModelAndView {
        val mav = ModelAndView("/sec/user")
        mav.model["user"] = user
        return mav
    }
}
