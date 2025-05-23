package ru.vassuv.testmediasofttask.warehouse.model.dto

import ru.vassuv.testmediasofttask.warehouse.model.domain.CreatedProduct
import java.math.BigDecimal

data class CreateProductRequestDto(
    val name: String,
    val article: String,
    val description: String?,
    val category: String?,
    val price: BigDecimal,
    val quantity: Int
)