package ch.jtaf.boundary.security

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.CodeSignature
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder.getContext

@Aspect
@Configuration
class OrganizationSecurityAspect(private val organizationAuthorizationChecker: OrganizationAuthorizationChecker) {

    @Around("@annotation(ch.jtaf.boundary.security.CheckOrganizationAccess)")
    fun around(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as CodeSignature

        signature.parameterNames.withIndex().forEach { (i, parameterName) ->
            if (parameterName == "organizationKey") {
                val organizationKey = joinPoint.args[i] as String
                val userHasAccessToOrganization = organizationAuthorizationChecker.userHasAccessToOrganization(getContext().authentication.name, organizationKey)
                if (!userHasAccessToOrganization) {
                    throw UserNotGrantedToAccessOrganizationException()
                } else {
                    return joinPoint.proceed()
                }
            }
        }
        throw IllegalStateException("No parameter with name organizationKey present!")
    }
}