package ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.handler

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.config.properties.S3Properties
import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.KafkaEvent
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest
import software.amazon.awssdk.services.s3.model.ObjectIdentifier

/**
 * Обработчик событий типа [KafkaEvent.Product.DeleteImage]
 */
@Component
class DeleteProductImageEventHandler(
    private val s3Client: S3Client,
    private val s3Properties: S3Properties
) : KafkaEventHandler<KafkaEvent.Product.DeleteImage> {


    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Метод проверки поддержки этим обработчиком - этого события [KafkaEvent]
     *
     * @param event проверяемое событие [KafkaEvent]
     * @return true если может обработать
     */
    override fun supports(event: KafkaEvent): Boolean =
        event is KafkaEvent.Product.DeleteImage

    /**
     * Метод обработки события [KafkaEvent.Product.DeleteImage]
     *
     * @param event обрабатываемое событие [KafkaEvent.Product.DeleteImage]
     */
    @Suppress("TooGenericExceptionCaught")
    override fun handle(event: KafkaEvent.Product.DeleteImage) {
        try {
            val keysToDelete = event.keys.map {
                ObjectIdentifier.builder().key(it).build()
            }

            s3Client.deleteObjects(
                DeleteObjectsRequest.builder()
                    .bucket(s3Properties.bucket)
                    .delete { it.objects(keysToDelete) }
                    .build()
            )

            logger.info("✅ Images deleted for product ${event.productId}")
        } catch (ex: Exception) {
            logger.error("❌ Failed to delete images for ${event.productId}", ex)
            // опционально: retry / save to DLQ
        }
    }
}

