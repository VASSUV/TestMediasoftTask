package ru.vassuv.testmediasofttask.warehouse.controller.response

import io.swagger.v3.oas.annotations.media.Schema
import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus

/**
 * Информация о результате подтверждения заказа.
 *
 * @property businessKey Идентификатор процесса подтверждения.
 * @property status Cтатус заказа [OrderStatus].
 */
@Schema(description = "Результат подтверждения заказа")
data class ConfirmOrderResponse(
    
    @field:Schema(description = "Идентификатор процесса подтверждения")
    val businessKey: String,
    
    @field:Schema(description = "Cтатус заказа")
    val status: OrderStatus
)
