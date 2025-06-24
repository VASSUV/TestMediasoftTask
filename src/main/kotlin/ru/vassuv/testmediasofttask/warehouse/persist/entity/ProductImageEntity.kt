package ru.vassuv.testmediasofttask.warehouse.persist.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "product_image")
data class ProductImageEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "product_id", nullable = false)
    val productId: UUID,

    @Column(name = "s3_key", nullable = false)
    val s3Key: String
)
