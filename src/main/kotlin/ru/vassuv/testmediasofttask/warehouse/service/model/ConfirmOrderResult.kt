package ru.vassuv.testmediasofttask.warehouse.service.model

import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus

/**
 * Информация о результате подтверждения заказа.
 *
 * @property businessKey Идентификатор процесса подтверждения.
 * @property status Cтатус заказа [OrderStatus].
 */ 
data class ConfirmOrderResult( 
    val businessKey: String, 
    val status: OrderStatus
)
