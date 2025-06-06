package ru.vassuv.testmediasofttask.warehouse.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import ru.vassuv.testmediasofttask.warehouse.controller.ProductController
import ru.vassuv.testmediasofttask.warehouse.enums.ProductCategoryType
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import java.math.BigDecimal
/**
 * DTO для обновления существующего товара ([ProductEntity]).
 *
 * Используется при обновлении данных товара через API ([ProductController.updateProduct]).
 */
data class UpdateProductRequest(
    /** Обновлённое название товара. Обязательное поле. */
    @field:NotBlank(message = "Название товара не должно быть пустым")
    @field:Schema(description = "Название товара", example = "Телевизор Samsung")
    val name: String,

    /** Обновлённый уникальный артикул товара. Обязательное поле. */
    @field:NotBlank(message = "Артикул не должен быть пустым")
    @field:Schema(description = "Артикул товара (уникальный)", example = "TV-5678")
    val article: String,

    /** Обновлённое описание товара. Необязательное поле. */
    @field:Schema(description = "Описание товара", example = "Телевизор Samsung QLED 55\"")
    val description: String? = null,

    /** Обновлённая категория товара ([ProductCategoryType]). Необязательное поле. */
    @field:Schema(description = "Категория товара", example = "ELECTRONICS")
    val category: ProductCategoryType? = null,

    /** Обновлённая цена товара. Обязательное поле, должна быть >= 0. */
    @field:NotNull(message = "Цена должна быть указана")
    @field:PositiveOrZero(message = "Цена должна быть >= 0")
    @field:Schema(description = "Цена товара", example = "34999.99")
    val price: BigDecimal,

    /** Обновлённое количество товара на складе. Не может быть отрицательным. */
    @field:Min(value = 0, message = "Количество не может быть отрицательным")
    @field:Schema(description = "Количество товара на складе", example = "20")
    val quantity: BigDecimal
)
