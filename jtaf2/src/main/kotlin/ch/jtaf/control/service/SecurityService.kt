package ch.jtaf.control.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService

class SecurityService(@Autowired private val authenticationManager: AuthenticationManager,
                      @Autowired private val userDetailsService: UserDetailsService) {

    fun autologin(username: String, password: String) {
        val userDetails = userDetailsService.loadUserByUsername(username)

        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(userDetails, password, userDetails.authorities)
        authenticationManager.authenticate(usernamePasswordAuthenticationToken)
    }
}