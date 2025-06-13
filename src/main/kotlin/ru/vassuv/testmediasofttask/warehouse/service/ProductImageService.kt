package ru.vassuv.testmediasofttask.warehouse.service

import org.springframework.web.multipart.MultipartFile
import java.io.OutputStream
import java.util.UUID

/**
 * Интерфейс сервиса для управления картинками в продуктах.
 *
 * Предоставляет операции для загрузки и скачивания картинок из продуктов.
 */
interface ProductImageService {

    /**
     * Позволяет загрузить файл изображения к продукту
     *
     * @param productId Идентификатор продукта
     * @param file Multipart file
     */
    fun uploadImage(productId: UUID, file: MultipartFile)

    /**
     * Позволяет скачать все изображения продукта zip архивом
     *
     * @param productId Идентификатор продукта
     * @param out [OutputStream]
     */
    fun downloadImagesAsZip(productId: UUID, out: OutputStream)
}
