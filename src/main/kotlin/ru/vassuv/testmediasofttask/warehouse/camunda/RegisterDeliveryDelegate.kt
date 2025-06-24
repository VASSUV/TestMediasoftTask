package ru.vassuv.testmediasofttask.warehouse.camunda

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.addressVar
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.DELIVERY_ID
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.EXPECTED_DATE
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.orderIdVar
import ru.vassuv.testmediasofttask.warehouse.interaction.rest.DeliveryServiceClient

@Component("registerDeliveryDelegate")
class RegisterDeliveryDelegate(
    private val deliveryServiceClient: DeliveryServiceClient
) : JavaDelegate {
    override fun execute(execution: DelegateExecution) {
        val orderId = execution.orderIdVar
        val address = execution.addressVar

        val response = deliveryServiceClient.registerDelivery(orderId, address)

        execution.setVariable(DELIVERY_ID, response.deliveryId.toString())
        execution.setVariable(EXPECTED_DATE, response.expectedDate)

        println("🚚 Доставка зарегистрирована: ${response.deliveryId}, дата: ${response.expectedDate}")
    }
}
