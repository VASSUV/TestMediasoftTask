package ru.vassuv.testmediasofttask.warehouse.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

/**
 * Исключение, возникающее, если продукт с указанным идентификатором не найден.
 *
 * @property id Идентификатор продукта, который не был найден.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class ProductNotFoundException(val id: UUID): RuntimeException("Товар с id=$id не найден")

/**
 * Метод вызывающий исключение ProductNotFoundException
 *
 * @property productId UUID продукта, вызвавшего ошибку
 * @throws ProductNotFoundException если товар не найден.
 */
fun productNotFoundError(productId: UUID): Nothing = throw ProductNotFoundException(productId)
