package ru.vassuv.testmediasofttask.warehouse.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.service.event.KafkaEvent
import ru.vassuv.testmediasofttask.warehouse.service.event.KafkaEventDispatcher

/**
 * Сервис для отслеживания сообщений из kafka
 *
 * @property objectMapper
 * @property dispatcher
 */
@Component
class KafkaEventConsumer(
    private val objectMapper: ObjectMapper,
    private val dispatcher: KafkaEventDispatcher
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Прослушивание текстовых сообщений
     *
     * @param message текстовое сообщение
     */
    @KafkaListener(
        topics = ["test_topic"],
        groupId = "test-consumer-group-1",
        containerFactory = "kafkaListenerContainerFactoryString",
    )
    fun listenTest(message: String) {
        logger.info("📥 Получено сообщение из Kafka: $message")
    }

    /**
     * Прослушивание bytearray сообщений
     *
     * @param record получаема запись из kafka key-value
     */
    @Suppress("TooGenericExceptionCaught")
    @KafkaListener(
        topics = ["warehouse_topic"],
        groupId = "warehouse-group",
        containerFactory = "kafkaListenerContainerFactoryByteArray",
    )
    fun handleMessage(record: ConsumerRecord<String, ByteArray>) {
        try {
            val event = objectMapper.readValue(record.value(), KafkaEvent::class.java)
            dispatcher.dispatch(event)
        } catch (ex: Exception) {
            logger.error("❌ Ошибка десериализации KafkaEvent: ${ex.message}", ex)
        }
    }
}
