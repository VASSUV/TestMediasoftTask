package ru.vassuv.paymentservice.controller.model

import java.math.BigDecimal
import java.util.UUID

data class PaymentRequest(val orderId: UUID, val accountNumber: String, val amount: BigDecimal)