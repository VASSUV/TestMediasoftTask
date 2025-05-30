package ru.vassuv.testmediasofttask.warehouse.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Ошибка существования продукта с уникальным артикулом
 *
 * @param article Артикул для которого сработала ошибка
 */
@ResponseStatus(HttpStatus.CONFLICT)
class ProductExistsWithArticleException(
    val article: String
): RuntimeException("Товар с таким артикулом уже существует")
