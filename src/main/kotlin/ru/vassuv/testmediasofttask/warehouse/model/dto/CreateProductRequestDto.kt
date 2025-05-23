package ru.vassuv.testmediasofttask.warehouse.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

/**
 * DTO для создания нового товара.
 */
data class CreateProductRequestDto(

    @field:Schema(description = "Название товара", example = "Телевизор")
    val name: String,

    @field:Schema(description = "Артикул товара (уникальный)", example = "TV-1234")
    val article: String,

    @field:Schema(description = "Описание товара", example = "4K OLED телевизор")
    val description: String?,

    @field:Schema(description = "Категория товара", example = "Электроника")
    val category: String?,

    @field:Schema(description = "Цена товара", example = "29999.99")
    val price: BigDecimal,

    @field:Schema(description = "Количество товара на складе", example = "15")
    val quantity: Int
)