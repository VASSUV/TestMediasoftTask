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
 * Сущность товара для хранения в базе данных.
 */
@Entity
@Table(name = "product")
class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, unique = true)
    var article: String,

    @Column(nullable = true, length = 1024)
    var description: String? = null,

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    var category: ProductCategoryType? = null,

    var price: BigDecimal,

    var quantity: BigDecimal,

    @CreationTimestamp
    @Column(nullable = false)
    var quantityUpdatedAt: ZonedDateTime = ZonedDateTime.now(),

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDate = LocalDate.now(),

    @Column(nullable = false)
    var isAvailable: Boolean = true
)
