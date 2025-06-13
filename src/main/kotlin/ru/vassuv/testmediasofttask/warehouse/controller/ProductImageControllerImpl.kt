package ru.vassuv.testmediasofttask.warehouse.controller

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.vassuv.testmediasofttask.warehouse.service.ProductImageService
import java.util.*

/**
 * REST-контроллер для управления картинками в продуктах.
 *
 * Предоставляет операции для загрузки и скачивания картинок из продуктов.
 */
@RestController
@RequestMapping("/api/products/{productId}/image")
class ProductImageControllerImpl(
    private val productImageService: ProductImageService
) : ProductImageController {

    /**
     * Позволяет загрузить файл изображения к продукту
     *
     * @param productId Идентификатор продукта
     * @param file Multipart file
     */
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun uploadImage(
        @PathVariable productId: UUID,
        @RequestParam("file") file: MultipartFile
    ) {
        productImageService.uploadImage(productId, file)
    }

    /**
     * Позволяет скачать все изображения продукта zip архивом
     *
     * @param productId Идентификатор продукта
     * @param response [HttpServletResponse]
     */
    @GetMapping("/download")
    override fun downloadZip(
        @PathVariable productId: UUID,
        response: HttpServletResponse
    ) {
        response.contentType = "application/zip"
        response.setHeader("Content-Disposition", "attachment; filename=product_$productId.zip")

        productImageService.downloadImagesAsZip(productId, response.outputStream)
    }
}
