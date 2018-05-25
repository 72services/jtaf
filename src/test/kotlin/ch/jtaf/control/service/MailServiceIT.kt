package ch.jtaf.control.service

import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class MailServiceIT {

    @Autowired
    lateinit var mailService: MailService

    @Test
    fun sendText() {
        mailService.sendText("noreply@jtaf.io", "simon@martinelli.ch", "Test Plain", "Test")
    }

    @Test
    fun sendHTML() {
        mailService.sendText("noreply@jtaf.io", "simon@martinelli.ch", "Test HTML", "<h1>Test</h1>")
    }
}