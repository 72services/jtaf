package ch.jtaf.boundary.web

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

class HttpContentProducer {

    fun getContentAsPdf(fileName: String, data: ByteArray): ResponseEntity<ByteArray> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("application/pdf")
        headers.setContentDispositionFormData(fileName, fileName)
        headers.cacheControl = "must-revalidate, post-check=0, pre-check=0"

        return ResponseEntity(data, headers, HttpStatus.OK)
    }

    fun getContentAsPng(fileName: String, data: ByteArray): ResponseEntity<ByteArray> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("image/png")
        headers.setContentDispositionFormData(fileName, fileName)
        headers.cacheControl = "must-revalidate, post-check=0, pre-check=0"

        return ResponseEntity(data, headers, HttpStatus.OK)
    }
}