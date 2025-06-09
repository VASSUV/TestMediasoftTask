package ru.vassuv.testmediasofttask.warehouse.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

/**
 * Конфигурация kafka.
 *
 * Используется для настройки consumers и producers.
 *
 * @see [KafkaProperties]
 */
@Configuration
@EnableKafka
class KafkaConfig(
    private val kafkaProperties: KafkaProperties
) {

    /**
     * Фабрика для KafkaListenerContainer <String, String>
     *
     * @return [ConcurrentKafkaListenerContainerFactory]
     */
    @Bean
    fun kafkaListenerContainerFactoryString(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = DefaultKafkaConsumerFactory(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers.first(),
                ConsumerConfig.GROUP_ID_CONFIG to kafkaProperties.consumer.groupId,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to kafkaProperties.consumer.autoOffsetReset
            )
        )
        return factory
    }

    /**
     * Фабрика для KafkaListenerContainer <String, ByteArray>
     *
     * @return [ConcurrentKafkaListenerContainerFactory]
     */
    @Bean
    fun kafkaListenerContainerFactoryByteArray(): ConcurrentKafkaListenerContainerFactory<String, ByteArray> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, ByteArray>()
        factory.consumerFactory = DefaultKafkaConsumerFactory(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers.first(),
                ConsumerConfig.GROUP_ID_CONFIG to kafkaProperties.consumer.groupId,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to ByteArrayDeserializer::class.java,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to kafkaProperties.consumer.autoOffsetReset
            )
        )
        return factory
    }

    /**
     * Шаблон для отправки сообщений в кафку  KafkaTemplate<String, String>
     *
     * @return [KafkaTemplate]
     */
    @Bean
    fun kafkaStringTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate<String, String>(
            DefaultKafkaProducerFactory(
                mapOf(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers.first(),
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                )
            )
        )
    }

    /**
     * Шаблон для отправки сообщений в кафку  KafkaTemplate<String, ByteArray>
     *
     * @return [KafkaTemplate]
     */
    @Bean
    fun kafkaByteArrayTemplate(): KafkaTemplate<String, ByteArray> {
        return KafkaTemplate<String, ByteArray>(
            DefaultKafkaProducerFactory(
                mapOf(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers.first(),
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to ByteArraySerializer::class.java,
                )
            )
        )
    }
}
