package ch.jtaf.config

import ch.jtaf.control.repository.SecurityGroupRepository
import ch.jtaf.control.repository.SecurityUserRepository
import ch.jtaf.entity.SecurityGroup
import ch.jtaf.entity.SecurityUser
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserInitializer(private val securityUserRepository: SecurityUserRepository,
                      private val securityGroupRepository: SecurityGroupRepository,
                      private val passwordEncoder: PasswordEncoder) : ApplicationRunner {

    @Transactional
    override fun run(args: ApplicationArguments) {
        var user = securityUserRepository.findByEmail("simon@martinelli.ch")

        if (user == null) {
            user = SecurityUser(null, "simon@martinelli.ch", passwordEncoder.encode("admin"), "Simon", "Martinelli", "", true)
            securityUserRepository.save(user)
        }

        var roleAdmin = securityGroupRepository.findByName("ADMIN")

        if (roleAdmin == null) {
            roleAdmin = SecurityGroup(null, "ADMIN")
            securityGroupRepository.save(roleAdmin)

            user.groups.add(roleAdmin)
        }

        var roleActuator = securityGroupRepository.findByName("ACTUATOR")

        if (roleActuator == null) {
            roleActuator = SecurityGroup(null, "ACTUATOR")
            securityGroupRepository.save(roleActuator)

            user.groups.add(roleActuator)
        }
    }
}