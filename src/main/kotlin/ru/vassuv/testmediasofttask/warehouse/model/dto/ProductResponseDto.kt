package ru.vassuv.testmediasofttask.warehouse.model.dto

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class ProductResponseDto(
    val id: UUID,
    val name: String,
    val article: String,
    val description: String?,
    val category: String?,
    val price: BigDecimal,
    val quantity: Int,
    val quantityUpdatedAt: LocalDateTime,
    val createdAt: LocalDateTime
)