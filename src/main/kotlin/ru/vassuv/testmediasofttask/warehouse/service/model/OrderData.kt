package ru.vassuv.testmediasofttask.warehouse.service.model

import ru.vassuv.testmediasofttask.warehouse.controller.response.OrderProductItemResponse
import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus
import java.math.BigDecimal
import java.util.UUID

/**
 * Ответ с информацией о заказе и его позициях.
 *
 * @property id Идентификатор заказа.
 * @property customerId Идентификатор заказчика.
 * @property status Статус заказа.
 * @property deliveryAddress Адрес доставки заказа.
 * @property totalPrice Стоимость заказа.
 * @property products Список продуктов в заказе.
 */
data class OrderData(
    val id: UUID,
    val customerId: UUID,
    val status: OrderStatus,
    val deliveryAddress: String,
    val totalPrice: BigDecimal,
    val products: List<OrderProductItem>
)

/**
 * Ограниченная информация о продукте в составе заказа.
 *
 * @property id Идентификатор продукта.
 * @property name Название продукта.
 * @property quantity Количество продукта в заказе.
 * @property price Зафиксированная цена продукта в заказе.
 */
data class OrderProductItem(
    val id: UUID,
    val name: String,
    val quantity: BigDecimal,
    val price: BigDecimal
)
