package ru.vassuv.currencyservice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.vassuv.currencyservice.service.CurrencyServiceException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CurrencyServiceException::class)
    fun handleServiceException(ex: CurrencyServiceException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(mapOf("error" to ex.message.orEmpty()))
    }
}