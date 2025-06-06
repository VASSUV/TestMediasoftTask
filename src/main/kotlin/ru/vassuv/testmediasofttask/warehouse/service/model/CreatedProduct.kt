package ru.vassuv.testmediasofttask.warehouse.service.model

import ru.vassuv.testmediasofttask.warehouse.enums.ProductCategoryType
import java.math.BigDecimal

/**
 * Доменная модель для создания нового товара в системе.
 *
 * @property name Название товара.
 * @property article Артикул товара.
 * @property description Описание товара (необязательно).
 * @property category Категория товара (необязательно).
 * @property price Цена товара.
 * @property quantity Количество товара на складе.
 */
data class CreatedProduct(
    val name: String,
    val article: String,
    val description: String?,
    val category: ProductCategoryType?,
    val price: BigDecimal,
    val quantity: BigDecimal
)