package ru.vassuv.paymentservice.controller.model

import java.util.UUID

data class PaymentResponse(
    val paymentId: UUID,
    val success: Boolean
)