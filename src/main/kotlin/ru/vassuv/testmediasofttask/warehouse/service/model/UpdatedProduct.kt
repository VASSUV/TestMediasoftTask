package ru.vassuv.testmediasofttask.warehouse.service.model

import ru.vassuv.testmediasofttask.warehouse.enums.ProductCategoryType
import java.math.BigDecimal

/**
 * Доменная модель для обновления существующего товара.
 *
 * @property name Новое название товара.
 * @property article Новый артикул товара.
 * @property description Новое описание товара (необязательно).
 * @property category Новая категория товара (необязательно).
 * @property price Новая цена товара.
 * @property quantity Новое количество товара на складе.
 */
data class UpdatedProduct(
    val name: String,
    val article: String,
    val description: String?,
    val category: ProductCategoryType?,
    val price: BigDecimal,
    val quantity: BigDecimal
)