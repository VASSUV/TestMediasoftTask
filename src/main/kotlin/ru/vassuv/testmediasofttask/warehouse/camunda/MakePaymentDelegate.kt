package ru.vassuv.testmediasofttask.warehouse.camunda

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.PAYMENT_SUCCESS
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.accountNumberVar
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.amountVar
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.orderIdVar
import ru.vassuv.testmediasofttask.warehouse.interaction.rest.PaymentServiceClient

@Component("makePaymentDelegate")
class MakePaymentDelegate(
    private val paymentServiceClient: PaymentServiceClient
) : JavaDelegate {

    override fun execute(execution: DelegateExecution) {
        val orderId = execution.orderIdVar
        val accountNumber = execution.accountNumberVar
        val amount = execution.amountVar

        val success = paymentServiceClient.makePayment(orderId, accountNumber, amount)

        execution.setVariable(PAYMENT_SUCCESS, success)
        println("💳 Оплата ${if (success) "успешна" else "неуспешна"} для заказа $orderId")
    }
}
