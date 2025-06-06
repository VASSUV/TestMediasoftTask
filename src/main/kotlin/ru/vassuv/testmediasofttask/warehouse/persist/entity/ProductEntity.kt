package ru.vassuv.testmediasofttask.warehouse.persist.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import ru.vassuv.testmediasofttask.warehouse.enums.ProductCategoryType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

/**
 * Сущность товара ([ProductEntity]), представляющая информацию о товарах на складе.
 *
 * Используется для хранения данных товаров и управления ими.
 */
@Entity
@Table(name = "product")
class ProductEntity(
    /** Уникальный идентификатор товара. Генерируется автоматически. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    /** Название товара. Обязательное поле. */
    @Column(nullable = false)
    var name: String,

    /** Уникальный артикул товара. Используется для поиска и идентификации. */
    @Column(nullable = false, unique = true)
    var article: String,

    /** Подробное описание товара. Максимальная длина — 1024 символа. */
    @Column(nullable = true, length = 1024)
    var description: String? = null,

    /** Категория товара ([ProductCategoryType]). Может быть не указана. */
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    var category: ProductCategoryType? = null,

    /** Текущая цена товара в валюте по умолчанию (обычно RUB). */
    var price: BigDecimal,

    /** Количество товара на складе. */
    var quantity: BigDecimal,

    /** Дата и время последнего изменения количества товара. */
    @CreationTimestamp
    @Column(nullable = false)
    var quantityUpdatedAt: ZonedDateTime = ZonedDateTime.now(),

    /** Дата создания записи о товаре. */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDate = LocalDate.now(),

    /** Флаг доступности товара для заказа. По умолчанию true (доступен). */
    @Column(nullable = false)
    var isAvailable: Boolean = true
)
