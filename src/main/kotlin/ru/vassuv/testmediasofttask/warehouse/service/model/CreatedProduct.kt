package ru.vassuv.testmediasofttask.warehouse.service.model

import java.math.BigDecimal

/**
 * Доменная модель для создания нового товара.
 */
data class CreatedProduct(
    val name: String,
    val article: String,
    val description: String?,
    val category: String?,
    val price: BigDecimal,
    val quantity: Int
)
