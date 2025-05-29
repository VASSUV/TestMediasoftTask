package ru.vassuv.testmediasofttask.warehouse.service.model

import ru.vassuv.testmediasofttask.warehouse.enums.ProductCategoryType
import java.math.BigDecimal

/**
 * Доменная модель для обновления товара.
 */
data class UpdatedProduct(
    val name: String,
    val article: String,
    val description: String?,
    val category: ProductCategoryType?,
    val price: BigDecimal,
    val quantity: BigDecimal
)
