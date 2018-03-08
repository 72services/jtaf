package ch.jtaf.boundary.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

class HttpContentUtil {

    fun getContentAsPdf(fileName: String, data: ByteArray): ResponseEntity<ByteArray> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("application/pdf")
        headers.setContentDispositionFormData(fileName, fileName)
        headers.cacheControl = "must-revalidate, post-check=0, pre-check=0"

        return ResponseEntity(data, headers, HttpStatus.OK)
    }
}