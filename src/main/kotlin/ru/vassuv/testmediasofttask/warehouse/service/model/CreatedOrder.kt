package ru.vassuv.testmediasofttask.warehouse.service.model

import java.math.BigDecimal
import java.util.*

/**
 * Доменная модель для создания нового заказа.
 *
 * @property deliveryAddress Адрес доставки.
 * @property items Список позиций товаров в заказе.
 */
data class CreatedOrder(
    val deliveryAddress: String,
    val items: List<CreatedOrderItem>
)

/**
 * Доменная модель позиции товара при создании нового заказа.
 *
 * @property productId Идентификатор товара.
 * @property quantity Количество товара в заказе.
 */
data class CreatedOrderItem(
    val productId: UUID,
    val quantity: BigDecimal
)