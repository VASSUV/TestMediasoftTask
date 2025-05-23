package ru.vassuv.testmediasofttask.warehouse.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/**
 * DTO-ответ с данными товара.
 */
data class ProductResponseDto(
    @field:Schema(description = "Идентификатор товара")
    val id: UUID,

    @field:Schema(description = "Название товара")
    val name: String,

    @field:Schema(description = "Артикул товара")
    val article: String,

    @field:Schema(description = "Описание товара")
    val description: String?,

    @field:Schema(description = "Категория товара")
    val category: String?,

    @field:Schema(description = "Цена товара")
    val price: BigDecimal,

    @field:Schema(description = "Количество товара на складе")
    val quantity: Int,

    @field:Schema(description = "Дата и время последнего изменения количества")
    val quantityUpdatedAt: LocalDateTime,

    @field:Schema(description = "Дата создания товара")
    val createdAt: LocalDateTime
)
