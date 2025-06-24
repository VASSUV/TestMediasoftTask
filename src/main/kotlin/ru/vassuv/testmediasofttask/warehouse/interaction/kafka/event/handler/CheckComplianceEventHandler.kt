package ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.handler

import org.camunda.bpm.engine.MismatchingMessageCorrelationException
import org.camunda.bpm.engine.RuntimeService
import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.KafkaEvent

/**
 * Обработчик событий типа [KafkaEvent.Compliance.CheckOrderRequest]
 */
@Component
class CheckComplianceEventHandler(
    private val runtimeService: RuntimeService
) : KafkaEventHandler<KafkaEvent.Compliance.CheckOrderRequest> {

    /**
     * Метод проверки поддержки этим обработчиком - этого события [KafkaEvent]
     *
     * @param event проверяемое событие [KafkaEvent]
     * @return true если может обработать
     */
    override fun supports(event: KafkaEvent): Boolean =
        event is KafkaEvent.Compliance.CheckOrderRequest

    /**
     * Метод обработки события [KafkaEvent.Compliance.CheckOrderRequest]
     *
     * @param event обрабатываемое событие [KafkaEvent.Compliance.CheckOrderRequest]
     */
    @Suppress("MagicNumber")
    override fun handle(event: KafkaEvent.Compliance.CheckOrderRequest) {
        println("🟢 Обработка CheckOrderRequest: $event")

        val isValid = !event.inn.endsWith("0")

        val start = System.currentTimeMillis()
        val timeoutMs = 5000L
        val sleepInterval = 200L
        val repeatCount = 25

        repeat (repeatCount) { i ->
            Thread.sleep(sleepInterval)
            try {
                runtimeService
                    .createMessageCorrelation("MessageWaitForCheckCompliance")
                    .processInstanceBusinessKey(event.businessKey)
                    .setVariable("isComplianceOk", isValid)
                    .correlate()
                println("📨 Отправлен результат проверки в процесс: businessKey=${event.businessKey}, valid=$isValid")
                return
            } catch (e: MismatchingMessageCorrelationException) {
                val key = event.businessKey
                println("❌ Ошибка отправки результата проверки в процесс: businessKey=$key, valid=$isValid")
                if (System.currentTimeMillis() - start > timeoutMs || i == repeatCount - 1) throw e
            }
        }
    }
}
