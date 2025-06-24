package ru.vassuv.paymentservice.service.model

import java.util.UUID

data class ProcessPaymentResult(val paymentId: UUID, val success: Boolean)