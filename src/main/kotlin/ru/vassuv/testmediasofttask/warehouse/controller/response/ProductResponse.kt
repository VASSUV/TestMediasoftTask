package ru.vassuv.testmediasofttask.warehouse.controller.response

import io.swagger.v3.oas.annotations.media.Schema
import ru.vassuv.testmediasofttask.warehouse.enums.ProductCategoryType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

/**
 * DTO-ответ с данными товара.
 */
data class ProductResponse(
    @field:Schema(description = "Идентификатор товара")
    val id: UUID,

    @field:Schema(description = "Название товара")
    val name: String,

    @field:Schema(description = "Артикул товара")
    val article: String,

    @field:Schema(description = "Описание товара")
    val description: String?,

    @field:Schema(description = "Категория товара")
    val category: ProductCategoryType?,

    @field:Schema(description = "Цена товара")
    val price: BigDecimal,

    @field:Schema(description = "Количество товара на складе")
    val quantity: BigDecimal,

    @field:Schema(description = "Дата и время последнего изменения количества")
    val quantityUpdatedAt: ZonedDateTime,

    @field:Schema(description = "Дата создания товара")
    val createdAt: LocalDate
)
