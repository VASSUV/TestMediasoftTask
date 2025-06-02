package ru.vassuv.testmediasofttask.warehouse.controller.response

import io.swagger.v3.oas.annotations.media.Schema
import ru.vassuv.testmediasofttask.warehouse.controller.ProductController
import ru.vassuv.testmediasofttask.warehouse.enums.ProductCategoryType
import ru.vassuv.testmediasofttask.warehouse.enums.CurrencyType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

/**
 * DTO для представления информации о товаре в ответах REST API.
 *
 * Используется в методах получения данных о товаре ([ProductController]).
 */
data class ProductResponse(
    /** Уникальный идентификатор товара. */
    @field:Schema(description = "Идентификатор товара")
    val id: UUID,

    /** Название товара. */
    @field:Schema(description = "Название товара")
    val name: String,

    /** Артикул товара. */
    @field:Schema(description = "Артикул товара")
    val article: String,

    /** Описание товара (необязательно). */
    @field:Schema(description = "Описание товара")
    val description: String?,

    /** Категория товара ([ProductCategoryType]), может отсутствовать. */
    @field:Schema(description = "Категория товара")
    val category: ProductCategoryType?,

    /** Цена товара в текущей валюте сессии. */
    @field:Schema(description = "Цена товара")
    val price: BigDecimal,

    /** Количество товара на складе. */
    @field:Schema(description = "Количество товара на складе")
    val quantity: BigDecimal,

    /** Дата и время последнего обновления количества. */
    @field:Schema(description = "Дата и время последнего изменения количества")
    val quantityUpdatedAt: ZonedDateTime,

    /** Дата создания записи о товаре. */
    @field:Schema(description = "Дата создания товара")
    val createdAt: LocalDate,

    /** Флаг доступности товара для заказа. */
    @field:Schema(description = "Доступность товара")
    val isAvailable: Boolean,

    /** Валюта цены товара. */
    @field:Schema(description = "Валюта возвращаемой цены")
    val currency: CurrencyType
)
