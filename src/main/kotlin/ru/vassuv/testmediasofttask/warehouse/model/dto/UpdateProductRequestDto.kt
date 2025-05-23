package ru.vassuv.testmediasofttask.warehouse.model.dto

import java.math.BigDecimal

data class UpdateProductRequestDto(
    val name: String,
    val article: String,
    val description: String?,
    val category: String?,
    val price: BigDecimal,
    val quantity: Int
)