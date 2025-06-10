package ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.handler

import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.KafkaEvent

/**
 * Обработчик событий типа [KafkaEvent.Order.Update]
 */
@Component
class UpdateOrderEventHandler : KafkaEventHandler<KafkaEvent.Order.Update> {

    /**
     * Метод проверки поддержки этим обработчиком - этого события [KafkaEvent]
     *
     * @param event проверяемое событие [KafkaEvent]
     * @return true если может обработать
     */
    override fun supports(event: KafkaEvent): Boolean =
        event is KafkaEvent.Order.Update

    /**
     * Метод обработки события [KafkaEvent.Order.Update]
     *
     * @param event обрабатываемое событие [KafkaEvent.Order.Update]
     */
    override fun handle(event: KafkaEvent.Order.Update) {
        println("🟢 Обработка UpdateOrder: $event")
    }
}
