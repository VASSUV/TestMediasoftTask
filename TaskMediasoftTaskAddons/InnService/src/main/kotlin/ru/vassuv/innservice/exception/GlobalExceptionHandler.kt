package ru.vassuv.innservice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(InnServiceException::class)
    fun handleServiceException(ex: InnServiceException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(mapOf("error" to ex.message.orEmpty()))
    }
}


@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
class InnServiceException(message: String) : RuntimeException(message)