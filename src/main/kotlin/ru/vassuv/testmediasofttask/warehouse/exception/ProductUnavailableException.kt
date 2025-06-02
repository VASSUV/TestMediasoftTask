package ru.vassuv.testmediasofttask.warehouse.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

/**
 * Ошибка, возникающая при попытке заказать продукт, который недоступен для заказа.
 *
 * Возможные причины:
 * - продукт отмечен как недоступный (isAvailable = false).
 * - недостаточное количество товара на складе.
 *
 * @property id UUID товара, вызвавшего ошибку
 * @property reason причина недоступности товара
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
class ProductUnavailableException(
    val id: UUID,
    val reason: String
) : RuntimeException("Товар с id=$id недоступен: $reason")