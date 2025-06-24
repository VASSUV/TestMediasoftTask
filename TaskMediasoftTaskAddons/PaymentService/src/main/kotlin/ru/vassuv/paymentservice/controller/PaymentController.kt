package ru.vassuv.paymentservice.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.vassuv.paymentservice.controller.model.PaymentRequest
import ru.vassuv.paymentservice.controller.model.PaymentResponse
import ru.vassuv.paymentservice.service.PaymentService

@RestController
@RequestMapping("/api")
class PaymentController(
    private val service: PaymentService,
) {
    @PostMapping("/pay")
    fun pay(@RequestBody req: PaymentRequest): PaymentResponse {
        val result = service.processPayment(req.orderId, req.accountNumber, req.amount)
        return PaymentResponse(result.paymentId, result.success)
    }
}