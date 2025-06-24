package ru.vassuv.deliveryservice.service.model

import java.time.LocalDate
import java.util.UUID

data class RegisterDeliveryResult(
    val deliverId: UUID,
    val expectedDate: LocalDate,
)