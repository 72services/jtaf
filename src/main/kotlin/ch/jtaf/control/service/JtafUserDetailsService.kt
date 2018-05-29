package ch.jtaf.control.service

import ch.jtaf.control.repository.SecurityUserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class JtafUserDetailsService(private val securityUserRepository: SecurityUserRepository) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(email: String): UserDetails? {
        val user = securityUserRepository.findByEmail(email)

        return if (user != null) {
            val grantedAuthorities = user.groups.map { SimpleGrantedAuthority(it.name) }.toSet()
            User(user.email, user.secret, grantedAuthorities)
        } else {
            throw UsernameNotFoundException(email)
        }
    }
}