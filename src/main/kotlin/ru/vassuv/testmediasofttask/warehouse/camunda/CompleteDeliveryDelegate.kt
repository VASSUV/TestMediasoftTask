package ru.vassuv.testmediasofttask.warehouse.camunda

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.deliveryIdVar
import ru.vassuv.testmediasofttask.warehouse.interaction.rest.DeliveryServiceClient

@Component("completeDeliveryDelegate")
class CompleteDeliveryDelegate(
    private val deliveryServiceClient: DeliveryServiceClient
) : JavaDelegate {

    override fun execute(execution: DelegateExecution) {
        val deliveryId = execution.deliveryIdVar
        deliveryServiceClient.completeDelivery(deliveryId)
        println("📦 Доставка $deliveryId завершена")
    }
}
