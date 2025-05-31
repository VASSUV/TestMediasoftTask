package ru.vassuv.testmediasofttask.warehouse.service.model

import ru.vassuv.testmediasofttask.warehouse.enums.ProductCategoryType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID

/**
 * Доменная модель товара.
 */
data class ProductData(
    val id: UUID,
    val name: String,
    val article: String,
    val description: String?,
    val category: ProductCategoryType?,
    val price: BigDecimal,
    val quantity: BigDecimal,
    val quantityUpdatedAt: ZonedDateTime,
    val createdAt: LocalDate,
    val isAvailable: Boolean
)
