package ru.vassuv.testmediasofttask.warehouse.service.event.handler

import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.service.event.KafkaEvent

/**
 * Обработчик событий типа [KafkaEvent.Order.UpdateStatus]
 */
@Component
class ChangeOrderStatusEventHandler : KafkaEventHandler<KafkaEvent.Order.UpdateStatus> {

    /**
     * Метод проверки поддержки этим обработчиком - этого события [KafkaEvent]
     *
     * @param event проверяемое событие [KafkaEvent]
     * @return true если может обработать
     */
    override fun supports(event: KafkaEvent): Boolean =
        event is KafkaEvent.Order.Delete

    /**
     * Метод обработки события [KafkaEvent.Order.UpdateStatus]
     *
     * @param event обрабатываемое событие [KafkaEvent.Order.UpdateStatus]
     */
    override fun handle(event: KafkaEvent.Order.UpdateStatus) {
        println("🔴 Удаление заказа: ${event.orderId}")
    }
}

