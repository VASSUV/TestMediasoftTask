package ru.vassuv.testmediasofttask.warehouse.service.event.handler

import ru.vassuv.testmediasofttask.warehouse.service.event.KafkaEvent

/**
 * Интерфейс обработчика всех типов событий kafka
 *
 * @param T тип события в виде дженерика
 */
interface KafkaEventHandler<T : KafkaEvent> {

    /**
     * Метод проверки поддержки этим обработчиком - этого события [KafkaEvent]
     *
     * @param event проверяемое событие [KafkaEvent]
     * @return true если может обработать
     */
    fun supports(event: KafkaEvent): Boolean

    /**
     * Метод обработки события [KafkaEvent]
     *
     * @param event обрабатываемое событие [KafkaEvent]
     */
    fun handle(event: T)
}

