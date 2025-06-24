package ru.vassuv.testmediasofttask.warehouse.interaction.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.KafkaEvent
import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.KafkaEventDispatcher

/**
 * Сервис для отслеживания сообщений из kafka
 *
 * @property objectMapper
 * @property dispatcher
 */
@Component
class KafkaConsumerImpl(
    private val objectMapper: ObjectMapper,
    private val dispatcher: KafkaEventDispatcher
): KafkaConsumer {

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
     * @param record получаемая запись из kafka key-value
     */
    @Suppress("TooGenericExceptionCaught")
    @KafkaListener(
        topics = [
            "warehouse_topic",
            "delete_product_image_topic",
            "check_compliance_topic"
        ],
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
