package ru.vassuv.testmediasofttask.warehouse.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import java.math.BigDecimal
import java.util.UUID

/**
 * Ошибка, возникающая при попытке заказать продукт, который недоступен для заказа.
 *
 * Возможные причины:
 * - продукт отмечен как недоступный (isAvailable = false).
 * - недостаточное количество товара на складе.
 *
 * @property productId UUID товара, вызвавшего ошибку
 * @property reason причина недоступности товара
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
class ProductUnavailableException(
    val productId: UUID,
    val reason: String
) : RuntimeException("Товар с id=$productId недоступен: $reason")

/**
 * Метод вызывающий исключение ProductUnavailableException
 *
 * @property productId UUID товара, вызвавшего ошибку
 * @throws ProductUnavailableException если товара недостаточно или он недоступен.
 */
fun productUnavailableError(productId: UUID, reason: String): Nothing =
    throw ProductUnavailableException(productId, reason)

/**
 * Метод проверяет доступность товара на складе
 *
 * @property productId UUID товара, вызвавшего ошибку
 * @throws ProductUnavailableException если товара недостаточно или он недоступен.
 */
fun ProductEntity.checkProductUnavailable(checkingQuantity: BigDecimal) {
    val productId = this.id!!
    if (!isAvailable) {
        productUnavailableError(productId, "Товар недоступен для заказа")
    }
    if (quantity < checkingQuantity) {
        productUnavailableError(productId, "Недостаточное количество товара на складе")
    }
}
