package ru.vassuv.testmediasofttask.warehouse.persist.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "order_product")
data class OrderProductEntity(
    @EmbeddedId
    val id: OrderProductId = OrderProductId(),

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    val order: OrderEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    val product: ProductEntity,

    @Column(nullable = false, precision = 15, scale = 2)
    val quantity: BigDecimal,

    @Column(nullable = false, precision = 15, scale = 2)
    val price: BigDecimal
)

@Embeddable
data class OrderProductId(
    @Column(name = "order_id")
    val orderId: UUID = UUID.randomUUID(),

    @Column(name = "product_id")
    val productId: UUID = UUID.randomUUID()
) : java.io.Serializable