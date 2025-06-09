package ru.vassuv.testmediasofttask.warehouse.service.event

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.service.event.handler.KafkaEventHandler

/**
 * Обработчик всех сообщений из kafka
 *
 * @property handlers список обработчиков наследовавшихся от [KafkaEventHandler]
 */
@Component
class KafkaEventDispatcher(
    private val handlers: List<KafkaEventHandler<*>>
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Метод обработчик всех сообщений
     * Если находит хоть один подходящий обработчик, то выполняет его
     *
     * @param event сообщение
     */
    fun dispatch(event: KafkaEvent) {
        val matching = handlers.find { it.supports(event) }

        if (matching != null) {
            @Suppress("UNCHECKED_CAST")
            (matching as KafkaEventHandler<KafkaEvent>).handle(event)
        } else {
            logger.warn("⚠ Нет обработчика для события: $event")
        }
    }
}