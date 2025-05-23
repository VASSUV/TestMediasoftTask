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

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>> {
        val errors = ex.bindingResult.allErrors.associate { error ->
            val fieldName = (error as FieldError).field
            fieldName to error.defaultMessage
        }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(
        ex: DataIntegrityViolationException
    ): ResponseEntity<Map<String, String>> {

        val message = ex.mostSpecificCause.message

        val userFriendlyMessage = when {
            // message?.contains("unique_article") == true || message?.contains("products_article_key") == true ->
            //     "Товар с таким артикулом уже существует!"
            else -> "Ошибка целостности данных!"
        }

        return ResponseEntity(
            mapOf("error" to userFriendlyMessage),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFoundException(ex: ProductNotFoundException): ResponseEntity<Map<String, String?>> {
        return ResponseEntity(
            mapOf("error" to "Товар с таким id=${ex.id} не найден"),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(ProductIsExistWithArticleException::class)
    fun handleProductIsExistWithArticleException(ex: ProductIsExistWithArticleException): ResponseEntity<Map<String, String?>> {
        return ResponseEntity(
            mapOf("error" to "Товар с таким артикулом уже существует!"),
            HttpStatus.BAD_REQUEST
        )
    }
}