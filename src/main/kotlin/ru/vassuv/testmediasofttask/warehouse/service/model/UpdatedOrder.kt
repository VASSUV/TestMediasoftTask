package ru.vassuv.testmediasofttask.warehouse.service.model

import java.math.BigDecimal
import java.util.*


/**
 * Запрос на изменение существующего заказа.
 *
 * @property items список изменяемых позиций заказа.
 */
data class UpdatedOrder(
    val deliveryAddress: String,
    val items: List<UpdatedOrderItem>
)

/**
 * Информация о конкретной позиции в изменении заказа.
 *
 * @property productId UUID товара.
 * @property quantity Новое количество товара.
 */
data class UpdatedOrderItem(
    val productId: UUID,
    val quantity: BigDecimal
)