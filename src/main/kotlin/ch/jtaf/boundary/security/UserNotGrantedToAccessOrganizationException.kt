package ch.jtaf.boundary.security

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User is not granted to access this organization")
class UserNotGrantedToAccessOrganizationException() : RuntimeException()