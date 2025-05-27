package ru.vassuv.testmediasofttask.warehouse.persist.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/**
 * Сущность товара для хранения в базе данных.
 */
@Entity
@Table(name = "products")
class ProductEntity(
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
    var category: String? = null, // TODO Enum

    var price: BigDecimal,

    var quantity: Int, // TODO BigDecimal

    @Column(nullable = false)
    var quantityUpdatedAt: LocalDateTime = LocalDateTime.now(), // TODO ZonedDateTime

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now() // TODO ZonedDateTime и по заданию дата нужна LocalDate,  в бд date(h2 может не знать)
)
