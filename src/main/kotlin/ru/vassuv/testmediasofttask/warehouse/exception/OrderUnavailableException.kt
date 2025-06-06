package ru.vassuv.testmediasofttask.warehouse.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

/**
 * Исключение, возникающее, если заказ с указанным идентификатором не доступен.
 *
 * @property id Идентификатор заказа, который доступен.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class OrderUnavailableException(val id: UUID) : RuntimeException("Заказ с id=$id доступен")