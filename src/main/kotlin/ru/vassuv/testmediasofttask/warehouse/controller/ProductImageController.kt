package ru.vassuv.testmediasofttask.warehouse.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.util.*

/**
 * Интерфейс REST-контроллера для управления картинками в продуктах.
 *
 * Предоставляет операции для загрузки и скачивания картинок из продуктов.
 */
interface ProductImageController {

    /**
     * Позволяет загрузить файл изображения к продукту
     *
     * @param productId Идентификатор продукта
     * @param file Multipart file
     */
    @Operation(
        summary = "Загрузить изображение товара",
        description = "Загружает изображение, связанное с productId, и сохраняет его в S3."
    )
    fun uploadImage(
        @PathVariable productId: UUID,
        @RequestParam("file") file: MultipartFile
    )

    /**
     * Позволяет скачать все изображения продукта zip архивом
     *
     * @param productId Идентификатор продукта
     * @param response [HttpServletResponse]
     */
    @Operation(
        summary = "Скачать изображения товара ZIP-архивом",
        description = "Возвращает все изображения по productId как ZIP-файл"
    )
    fun downloadZip(
        @PathVariable productId: UUID,
        response: HttpServletResponse
    )
}
