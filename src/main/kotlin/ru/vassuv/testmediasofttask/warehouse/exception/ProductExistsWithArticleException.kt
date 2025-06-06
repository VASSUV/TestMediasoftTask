package ru.vassuv.testmediasofttask.warehouse.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Исключение, возникающее при попытке создать продукт с уже существующим артикулом.
 *
 * @property article Артикул, вызвавший конфликт.
 */
@ResponseStatus(HttpStatus.CONFLICT)
class ProductExistsWithArticleException(
    val article: String
): RuntimeException("Товар с таким артикулом уже существует")
