package ru.vassuv.testmediasofttask.warehouse.controller.params

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

/**
 * Параметры простого многокритериального поиска продуктов.
 *
 * Содержит набор возможных фильтров, каждый из которых является опциональным.
 */
data class ProductSearchParams(
    /** Список идентификаторов продуктов для поиска */
    val ids: List<UUID>? = null,

    /** Название продукта (поиск по частичному совпадению) */
    val name: String? = null,

    /** Артикул продукта (поиск по частичному совпадению) */
    val article: String? = null,

    /** Описание продукта (поиск по частичному совпадению) */
    val description: String? = null,

    /** Категория продукта */
    val category: String? = null,

    /** Минимальная цена продукта */
    val minPrice: BigDecimal? = null,

    /** Максимальная цена продукта */
    val maxPrice: BigDecimal? = null,

    /** Минимальное количество продуктов на складе */
    val minQuantity: BigDecimal? = null,

    /** Максимальное количество продуктов на складе */
    val maxQuantity: BigDecimal? = null,

    /** Дата создания продукта, начиная с указанной даты */
    val createdAfter: LocalDate? = null,

    /** Дата создания продукта, до указанной даты */
    val createdBefore: LocalDate? = null,

    /** Дата последнего изменения количества продукта, начиная с указанной даты и времени */
    val updatedAfter: LocalDateTime? = null,

    /** Дата последнего изменения количества продукта, до указанной даты и времени */
    val updatedBefore: LocalDateTime? = null,
)