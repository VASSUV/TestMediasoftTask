package ru.vassuv.testmediasofttask.warehouse.model.dbo

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/**
 * Сущность товара для хранения в базе данных.
 */
@Entity
@Table(name = "products")
class ProductDbo(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, unique = true)
    var article: String,

    @Column(nullable = true)
    var description: String? = null,

    @Column(nullable = true)
    var category: String? = null,

    var price: BigDecimal,

    var quantity: Int,

    @Column(nullable = false)
    var quantityUpdatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)