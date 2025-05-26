package ru.vassuv.testmediasofttask.warehouse.exception.handler

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import kotlin.reflect.full.findAnnotation

/**
 * Глобальный обработчик исключений.
 * Перехватывает ошибки и возвращает пользователю удобные сообщения.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    /**
     * Обработка всех исключений приложения
     */
    @ExceptionHandler(Exception::class)
    fun handleAllUncaughtException(ex: Exception): ResponseEntity<ApiError> {
        val annotationStatus = ex.resolveAnnotatedStatus() ?: HttpStatus.INTERNAL_SERVER_ERROR
        val statusCode = annotationStatus.value()
        val statusName = annotationStatus.name
        return ResponseEntity (ApiError(statusCode, statusName, ex.localizedMessage), annotationStatus)
    }

    /**
     * Обрабатывает исключения валидации.
     *
     * @param ex Исключение, возникающее при ошибке валидации.
     * @return Ответ с описанием ошибок.
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>> {
        val errors = ex.bindingResult.allErrors.associate { error ->
            val fieldName = (error as FieldError).field
            fieldName to error.defaultMessage
        }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    /**
     * Поиск статуса из аннотации к ошибке
     *
     * @param ex проверяемая ошибка
     * @return Статус указанный в аннотацие
     */
    private fun Exception.resolveAnnotatedStatus(): HttpStatus? =
        this::class.findAnnotation<ResponseStatus>()?.value
}
