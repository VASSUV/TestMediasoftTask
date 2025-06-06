package ru.vassuv.testmediasofttask.warehouse.service.model

import ru.vassuv.testmediasofttask.warehouse.enums.ProductCategoryType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID

/**
 * Полная доменная модель товара.
 *
 * @property id Идентификатор товара.
 * @property name Название товара.
 * @property article Артикул товара.
 * @property description Описание товара (необязательно).
 * @property category Категория товара (необязательно).
 * @property price Цена товара.
 * @property quantity Количество товара на складе.
 * @property quantityUpdatedAt Дата и время последнего изменения количества товара.
 * @property createdAt Дата создания товара.
 * @property isAvailable Доступность товара на складе.
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
