package ru.vassuv.testmediasofttask.warehouse.camunda

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.innVar
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.loginVar
import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.KafkaProducer
import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.KafkaEvent
import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.KafkaTopic

@Component("checkComplianceDelegate")
class CheckComplianceDelegate(
    private val kafkaProducer: KafkaProducer
) : JavaDelegate {

    override fun execute(execution: DelegateExecution) {
        val businessKey = execution.processBusinessKey

        val event = KafkaEvent.Compliance.CheckOrderRequest(
            login = execution.loginVar,
            inn = execution.innVar,
            businessKey = businessKey
        )

        kafkaProducer.sendEvent(KafkaTopic.CHECK_COMPLIANCE, event)
    }
}
