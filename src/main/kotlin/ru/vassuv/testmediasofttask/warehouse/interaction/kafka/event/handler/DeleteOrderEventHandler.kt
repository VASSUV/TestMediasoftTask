package ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.handler

import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.KafkaEvent

/**
 * Обработчик событий типа [KafkaEvent.Order.Delete]
 */
@Component
class DeleteOrderEventHandler : KafkaEventHandler<KafkaEvent.Order.Delete> {

    /**
     * Метод проверки поддержки этим обработчиком - этого события [KafkaEvent]
     *
     * @param event проверяемое событие [KafkaEvent]
     * @return true если может обработать
     */
    override fun supports(event: KafkaEvent): Boolean =
        event is KafkaEvent.Order.Delete

    /**
     * Метод обработки события [KafkaEvent.Order.Delete]
     *
     * @param event обрабатываемое событие [KafkaEvent.Order.Delete]
     */
    override fun handle(event: KafkaEvent.Order.Delete) {
        println("🔴 Удаление заказа: ${event.orderId}")
    }
}
