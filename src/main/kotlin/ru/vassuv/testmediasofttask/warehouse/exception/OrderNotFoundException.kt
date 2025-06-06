package ru.vassuv.testmediasofttask.warehouse.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

/**
 * Исключение, возникающее, если заказ с указанным идентификатором не найден.
 *
 * @property id Идентификатор заказа, который не был найден.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class OrderNotFoundException(val id: UUID) : RuntimeException("Заказ с id=$id не найден")