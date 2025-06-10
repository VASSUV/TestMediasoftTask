package ru.vassuv.testmediasofttask.warehouse.interaction.kafka

import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.KafkaEvent
import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.KafkaTopic

/**
 * Сервис для отправки сообщений в kafka
 */
interface KafkaProducer {

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
