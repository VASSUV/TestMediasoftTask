package ru.vassuv.testmediasofttask.warehouse.controller.response

import io.swagger.v3.oas.annotations.media.Schema
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
 * @property products Список продуктов в заказе.
 */
@Schema(description = "Информация о заказе")
data class OrderResponse(

    @field:Schema(description = "Уникальный идентификатор заказа")
    val id: UUID,

    @field:Schema(description = "Идентификатор заказчика, создавшего заказ")
    val customerId: UUID,

    @field:Schema(description = "Статус заказа", example = "CREATED")
    val status: OrderStatus,

    @field:Schema(description = "Адрес доставки заказа", example = "ул. Пушкина, дом 10")
    val deliveryAddress: String,

    @field:Schema(description = "Стоимость заказа", example = "12000")
    val totalPrice: BigDecimal,

    @field:Schema(description = "Список товаров в заказе")
    val products: List<OrderProductItemResponse>
)

/**
 * Ограниченная информация о продукте в составе заказа.
 *
 * @property id Идентификатор продукта.
 * @property name Название продукта.
 * @property quantity Количество продукта в заказе.
 * @property price Зафиксированная цена продукта в заказе.
 */
@Schema(description = "Товар в составе заказа")
data class OrderProductItemResponse(

    @field:Schema(description = "Идентификатор товара")
    val id: UUID,

    @field:Schema(description = "Название товара")
    val name: String,

    @field:Schema(description = "Количество товара в заказе", example = "2")
    val quantity: BigDecimal,

    @field:Schema(description = "Цена товара на момент оформления заказа", example = "1500.00")
    val price: BigDecimal
)