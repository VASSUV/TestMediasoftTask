package ru.vassuv.testmediasofttask.warehouse.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus
import java.util.UUID

/**
 * Ошибка, возникающая при попытке выполнить действие с заказом в недопустимом статусе.
 *
 * @property orderId идентификатор заказа.
 * @property currentStatus текущий статус заказа.
 * @property message дополнительная информация об ошибке.
 */
@ResponseStatus(HttpStatus.CONFLICT)
class IllegalOrderStatusException(
    val orderId: UUID,
    val currentStatus: OrderStatus,
    message: String
) : RuntimeException("Невозможно выполнить действие с заказом id=$orderId в статусе $currentStatus: $message")
