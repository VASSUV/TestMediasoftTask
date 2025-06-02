package ru.vassuv.testmediasofttask.warehouse.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import ru.vassuv.testmediasofttask.warehouse.controller.OrderController
import ru.vassuv.testmediasofttask.warehouse.persist.entity.OrderEntity
import java.math.BigDecimal
import java.util.UUID

/**
 * DTO для создания нового заказа ([OrderEntity]).
 *
 * Используется при создании заказа через API ([OrderController.createOrder]).
 */
data class CreateOrderRequest(
    /** Адрес доставки заказа. Обязательное поле. */
    @field:NotNull(message = "Адрес доставки обязателен")
    @field:Schema(description = "Адрес доставки заказа", example = "ул. Пушкина, дом 10")
    val deliveryAddress: String,

    /** Список товаров, входящих в заказ. Не должен быть пустым. */
    @field:NotEmpty(message = "Заказ должен содержать хотя бы один товар")
    @field:Valid
    val items: List<CreateOrderItemRequest>
)

/**
 * DTO для товара в составе создаваемого заказа.
 */
data class CreateOrderItemRequest(
    /** Идентификатор товара. Обязательное поле. */
    @field:NotNull(message = "Идентификатор товара обязателен")
    val productId: UUID,

    /** Количество товара для заказа. Обязательное поле. */
    @field:NotNull(message = "Количество товара должно быть указано")
    @field:Schema(description = "Количество товара", example = "3")
    val quantity: BigDecimal
)