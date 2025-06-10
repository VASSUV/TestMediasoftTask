package ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.handler

import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.KafkaEvent

/**
 * Обработчик событий типа [KafkaEvent.Order.Create]
 */
@Component
class CreateOrderEventHandler : KafkaEventHandler<KafkaEvent.Order.Create> {

    /**
     * Метод проверки поддержки этим обработчиком - этого события [KafkaEvent]
     *
     * @param event проверяемое событие [KafkaEvent]
     * @return true если может обработать
     */
    override fun supports(event: KafkaEvent): Boolean =
        event is KafkaEvent.Order.Create

    /**
     * Метод обработки события [KafkaEvent.Order.Create]
     *
     * @param event обрабатываемое событие [KafkaEvent.Order.Create]
     */
    override fun handle(event: KafkaEvent.Order.Create) {
        println("🟢 Обработка CreateOrder: $event")
    }
}
