package ru.vassuv.testmediasofttask.warehouse.exception.handler

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.vassuv.testmediasofttask.warehouse.exception.ProductIsExistWithArticleException
import ru.vassuv.testmediasofttask.warehouse.exception.ProductNotFoundException

/**
 * Глобальный обработчик исключений.
 * Перехватывает ошибки и возвращает пользователю удобные сообщения.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

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
     * Обрабатывает ошибки целостности данных (например, уникальность поля).
     *
     * @param ex Исключение нарушения ограничений БД.
     * @return Ответ с описанием ошибки.
     */
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(
        ex: DataIntegrityViolationException
    ): ResponseEntity<Map<String, String>> {

        val message = ex.mostSpecificCause.message

        val userFriendlyMessage = when {
            // Example:
            // message?.contains("unique_article") == true || message?.contains("products_article_key") == true ->
            //     "Товар с таким артикулом уже существует!"
            else -> "Ошибка целостности данных!"
        }

        return ResponseEntity(
            mapOf("error" to userFriendlyMessage),
            HttpStatus.BAD_REQUEST
        )
    }

    /**
     * Обрабатывает ошибки отсутствия продукта в БД
     *
     * @param ex Исключение отсутствия продукта в БД
     * @return Ответ с описанием ошибки.
     */
    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFoundException(ex: ProductNotFoundException): ResponseEntity<Map<String, String?>> {
        return ResponseEntity(
            mapOf("error" to "Товар с таким id=${ex.id} не найден"),
            HttpStatus.NOT_FOUND
        )
    }

    /**
     * Обрабатывает ошибки существования продукта с уникальным артикулом
     *
     * @param ex Исключение существования продукта с уникальным артикулом
     * @return Ответ с описанием ошибки.
     */
    @ExceptionHandler(ProductIsExistWithArticleException::class)
    fun handleProductIsExistWithArticleException(ex: ProductIsExistWithArticleException): ResponseEntity<Map<String, String?>> {
        return ResponseEntity(
            mapOf("error" to "Товар с таким артикулом уже существует!"),
            HttpStatus.BAD_REQUEST
        )
    }
}