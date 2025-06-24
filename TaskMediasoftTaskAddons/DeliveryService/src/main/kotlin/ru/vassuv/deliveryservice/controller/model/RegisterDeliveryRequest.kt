package ru.vassuv.deliveryservice.controller.model

import java.util.UUID

data class RegisterDeliveryRequest(val orderId: UUID, val address: String)