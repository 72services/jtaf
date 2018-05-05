package ch.jtaf.boundary.dto

class Message(val level: String, val message: String) {

    companion object {
        const val success = "success"
        const val danger = "danger"
    }
}

