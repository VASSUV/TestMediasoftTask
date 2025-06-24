
package ru.vassuv.paymentservice.service

import org.springframework.stereotype.Service
import ru.vassuv.paymentservice.persists.PaymentEntity
import ru.vassuv.paymentservice.persists.PaymentRepository
import ru.vassuv.paymentservice.service.model.ProcessPaymentResult
import java.math.BigDecimal
import java.util.UUID


@Service
class PaymentService(
    private val repository: PaymentRepository
) {
    fun processPayment(orderId: UUID, accountNumber: String, amount: BigDecimal): ProcessPaymentResult {
        // условная логика: если сумма нечётная — успех
        val success = amount.remainder(BigDecimal(2)) != BigDecimal.ZERO

        val result = repository.save(
            PaymentEntity(
                orderId = orderId,
                accountNumber = accountNumber,
                amount = amount,
                success = success
            )
        )

        return ProcessPaymentResult(
            paymentId = result.id!!,
            success = success
        )
    }
}
