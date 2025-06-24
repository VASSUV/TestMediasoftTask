package ru.vassuv.accountnumbersservice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CurrencyServiceException::class)
    fun handleServiceException(ex: CurrencyServiceException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(mapOf("error" to ex.message.orEmpty()))
    }
}

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
class CurrencyServiceException(message: String) : RuntimeException(message)