package ru.vassuv.testmediasofttask.warehouse.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductImageEntity
import ru.vassuv.testmediasofttask.warehouse.persist.repository.ProductImageRepository
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.OutputStream
import java.time.ZonedDateTime
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Сервис для управления картинками в продуктах.
 *
 * Предоставляет операции для загрузки и скачивания картинок из продуктов.
 */
@Service
class ProductImageServiceImpl(
    private val s3Client: S3Client,
    private val productImageRepository: ProductImageRepository,
    @Value("\${s3.bucket}") private val bucketName: String
) : ProductImageService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Позволяет загрузить файл изображения к продукту
     *
     * @param productId Идентификатор продукта
     * @param file Multipart file
     */
    @Suppress("TooGenericExceptionCaught")
    override fun uploadImage(productId: UUID, file: MultipartFile) {
        try {
            val key = "products/${productId}/${ZonedDateTime.now()}_${file.originalFilename}"

            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.contentType ?: "application/octet-stream")
                    .build(),
                RequestBody.fromInputStream(file.inputStream, file.size)
            )

            productImageRepository.save(
                ProductImageEntity(
                    productId = productId,
                    s3Key = key
                )
            )
        } catch (ex: Exception) {
            logger.error("❌ Ошибка при загрузке файла: ${ex.message}", ex)
            throw ex
        }
    }

    /**
     * Позволяет скачать все изображения продукта zip архивом
     *
     * @param productId Идентификатор продукта
     * @param out [OutputStream]
     */
    override fun downloadImagesAsZip(productId: UUID, out: OutputStream) {
        val imageRecords = productImageRepository.findAllByProductId(productId)

        ZipOutputStream(out).use { zipStream ->
            imageRecords.forEach { image ->
                val objectResponse = s3Client.getObject(
                    GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(image.s3Key)
                        .build()
                )

                zipStream.putNextEntry(ZipEntry(image.s3Key.substringAfterLast('/')))
                objectResponse.use { input ->
                    input.copyTo(zipStream)
                }
                zipStream.closeEntry()
            }
        }
    }
}
