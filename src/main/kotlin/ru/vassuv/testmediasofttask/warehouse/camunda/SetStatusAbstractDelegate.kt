package ru.vassuv.testmediasofttask.warehouse.camunda

import camundajar.impl.scala.math.BigDecimal
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.orderIdVar
import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus
import ru.vassuv.testmediasofttask.warehouse.service.OrderService

abstract class SetStatusAbstractDelegate(
    private val orderService: OrderService,
    private val status: OrderStatus
): JavaDelegate {

    override fun execute(execution: DelegateExecution) {
        val orderId = execution.orderIdVar
        val businessKey = execution.processBusinessKey

        orderService.updateStatusAndBusinessKey(
            orderId = orderId,
            status = status,
            businessKey = businessKey
        )

        println("🟢Статус заказа успешно изменен на $status")
    }
}
