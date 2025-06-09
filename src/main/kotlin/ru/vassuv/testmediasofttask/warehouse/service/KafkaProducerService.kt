package ru.vassuv.testmediasofttask.warehouse.service

import ru.vassuv.testmediasofttask.warehouse.service.event.KafkaEvent
import ru.vassuv.testmediasofttask.warehouse.service.event.KafkaTopic

/**
 * Сервис для отправки сообщений в kafka
 */
interface KafkaProducerService {

    /**
     * Отправка текстового сообщения
     *
     * @param topic название топика [KafkaTopic]
     * @param message тестовое сообщение
     * @param key ключ к сообщению
     */
    fun sendStringMessage(topic: KafkaTopic, message: String, key: String? = null)

    /**
     * Отправка сообщения [KafkaEvent]
     *
     * @param topic название топика [KafkaTopic]
     * @param event сообщение [KafkaEvent]
     * @param key ключ к сообщению
     */
    fun sendEvent(topic: KafkaTopic, event: KafkaEvent, key: String? = null)
}
