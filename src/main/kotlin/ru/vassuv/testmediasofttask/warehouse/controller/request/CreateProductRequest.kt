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
 * DTO для передачи данных при создании нового товара ([ProductEntity]).
 *
 * Используется в методе создания товара контроллера ([ProductController.createProduct]).
 */
data class CreateProductRequest(

    /** Название товара. Обязательное поле, не должно быть пустым. */
    @field:NotBlank(message = "Название товара не должно быть пустым")
    @field:Schema(description = "Название товара", example = "Телевизор")
    val name: String,

    /** Уникальный артикул товара. Обязательное поле, не должно быть пустым. */
    @field:NotBlank(message = "Артикул не должен быть пустым")
    @field:Schema(description = "Артикул товара (уникальный)", example = "TV-1234")
    val article: String,

    /** Подробное описание товара. Необязательное поле. */
    @field:Schema(description = "Описание товара", example = "4K OLED телевизор")
    val description: String?,

    /** Категория товара ([ProductCategoryType]). Необязательное поле. */
    @field:Schema(description = "Категория товара", example = "ELECTRONICS")
    val category: ProductCategoryType?,

    /** Цена товара. Обязательное поле, должна быть >= 0. */
    @field:NotNull(message = "Цена должна быть указана")
    @field:PositiveOrZero(message = "Цена должна быть >= 0")
    @field:Schema(description = "Цена товара", example = "29999.99")
    val price: BigDecimal,

    /** Количество товара на складе. Не может быть отрицательным. */
    @field:Min(value = 0, message = "Количество не может быть отрицательным")
    @field:Schema(description = "Количество товара на складе", example = "15")
    val quantity: BigDecimal
)
