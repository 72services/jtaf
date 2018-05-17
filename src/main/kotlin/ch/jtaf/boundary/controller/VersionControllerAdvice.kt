package ch.jtaf.boundary.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

@ControllerAdvice
class VersionControllerAdvice {

    @Value("\${application.version}")
    val applicationVersion: String? = null

    @ModelAttribute("applicationVersion")
    fun provideApplicationVersion(): String? {
        return applicationVersion
    }
}