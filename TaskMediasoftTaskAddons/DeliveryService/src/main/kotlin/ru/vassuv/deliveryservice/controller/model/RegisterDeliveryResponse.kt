package ru.vassuv.deliveryservice.controller.model

import java.time.LocalDate
import java.util.UUID

data class RegisterDeliveryResponse(
    val deliveryId: UUID,
    val expectedDate: LocalDate
)