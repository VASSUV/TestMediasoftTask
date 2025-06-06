package ru.vassuv.testmediasofttask.warehouse.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import ru.vassuv.testmediasofttask.warehouse.controller.OrderController
import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus
import ru.vassuv.testmediasofttask.warehouse.persist.entity.OrderEntity

/**
 * DTO для обновления статуса существующего заказа ([OrderEntity]).
 *
 * Используется в методах изменения статуса заказа ([OrderController.updateOrderStatus]).
 */
data class UpdateOrderStatusRequest(
    /** Новый статус заказа. Обязательное поле. */
    @field:NotNull(message = "Статус заказа должен быть указан")
    @field:Schema(description = "Новый статус заказа", example = "CONFIRMED")
    val status: OrderStatus
)
