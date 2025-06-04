package ru.vassuv.testmediasofttask.warehouse.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import ru.vassuv.testmediasofttask.warehouse.controller.OrderController
import ru.vassuv.testmediasofttask.warehouse.persist.entity.OrderEntity
import java.math.BigDecimal
import java.util.UUID

/**
 * DTO для обновления существующего заказа ([OrderEntity]).
 *
 * Используется для изменения состава и количества товаров в заказе через API ([OrderController.updateOrder]).
 */
data class UpdateOrderRequest(
    /** Обновлённый адрес доставки. Обязательное поле. */
    @field:NotNull(message = "Адрес доставки должен быть указан")
    @field:Schema(description = "Новый адрес доставки", example = "ул. Лермонтова, дом 15")
    val deliveryAddress: String,

    /** Обновлённый список товаров в заказе. Не должен быть пустым. */
    @field:NotEmpty(message = "В заказе должен быть минимум один товар")
    @field:Valid
    val items: List<UpdateOrderItemRequest>
)

/**
 * DTO для товара в составе обновляемого заказа.
 */
data class UpdateOrderItemRequest(
    /** Идентификатор товара. Обязательное поле. */
    @field:NotNull(message = "Идентификатор товара обязателен")
    val productId: UUID,

    /** Количество товара в заказе. Обязательное поле. */
    @field:NotNull(message = "Количество товара должно быть указано")
    @field:Schema(description = "Количество товара", example = "2")
    @field:Positive(message = "Количество товара должно быть больше 0")
    val quantity: BigDecimal
)