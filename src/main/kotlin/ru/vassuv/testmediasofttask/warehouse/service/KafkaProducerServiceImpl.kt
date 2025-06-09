package ru.vassuv.testmediasofttask.warehouse.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.vassuv.testmediasofttask.warehouse.service.event.KafkaEvent
import ru.vassuv.testmediasofttask.warehouse.service.event.KafkaTopic

/**
 * Сервис для отправки сообщений в kafka
 */
@Service
class KafkaProducerServiceImpl(
    private val objectMapper: ObjectMapper,
    private val kafkaStringTemplate: KafkaTemplate<String, String>,
    private val kafkaByteArrayTemplate: KafkaTemplate<String, ByteArray>
) : KafkaProducerService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Отправка текстового сообщения
     *
     * @param topic название топика [KafkaTopic]
     * @param message тестовое сообщение
     * @param key ключ к сообщению
     */
    override fun sendStringMessage(topic: KafkaTopic, message: String, key: String?) {
        logger.info("📤 Отправка KafkaEvent в Kafka [topic=${topic.topicName}, key=$key]: $message")
        if (key != null) {
            kafkaStringTemplate.send(topic.topicName, key, message)
        } else {
            kafkaStringTemplate.send(topic.topicName, message)
        }
    }

    /**
     * Отправка сообщения [KafkaEvent]
     *
     * @param topic название топика [KafkaTopic]
     * @param event сообщение [KafkaEvent]
     * @param key ключ к сообщению
     */
    override fun sendEvent(topic: KafkaTopic, event: KafkaEvent, key: String?) {
        val json = objectMapper.writeValueAsBytes(event) // 🔁 сериализация в ByteArray
        logger.info("📤 Отправка KafkaEvent в Kafka [topic=${topic.topicName}, key=$key]: ${String(json)}")

        if (key != null) {
            kafkaByteArrayTemplate.send(topic.topicName, key, json)
        } else {
            kafkaByteArrayTemplate.send(topic.topicName, json)
        }
    }
}
