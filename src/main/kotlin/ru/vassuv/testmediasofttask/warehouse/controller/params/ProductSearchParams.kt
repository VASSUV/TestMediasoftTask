package ru.vassuv.testmediasofttask.warehouse.controller.params

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class ProductSearchParams(
    val ids: List<UUID>? = null,
    val name: String? = null,
    val article: String? = null,
    val description: String? = null,
    val category: String? = null,
    val minPrice: BigDecimal? = null,
    val maxPrice: BigDecimal? = null,
    val minQuantity: BigDecimal? = null,
    val maxQuantity: BigDecimal? = null,
    val createdAfter: LocalDate? = null,
    val createdBefore: LocalDate? = null,
    val updatedAfter: LocalDateTime? = null,
    val updatedBefore: LocalDateTime? = null,
)