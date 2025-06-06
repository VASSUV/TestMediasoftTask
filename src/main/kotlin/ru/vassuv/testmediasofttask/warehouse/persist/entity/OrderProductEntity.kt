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
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.math.BigDecimal
import java.util.UUID

/**
 * Промежуточная сущность ([OrderProductEntity]) для связи заказов ([OrderEntity]) и товаров ([ProductEntity]).
 *
 * Использует составной ключ [OrderProductId] для идентификации уникальной пары (заказ, товар).
 * Хранит информацию о товарах в конкретном заказе: количество и цена на момент оформления заказа.
 */
@Entity
@Table(name = "order_product")
data class OrderProductEntity(
    /** Составной ключ, состоящий из идентификаторов заказа и товара. */
    @EmbeddedId
    val id: OrderProductId = OrderProductId(),

    /** Связь с заказом ([OrderEntity]). Идентификатор заказа является частью ключа. */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    val order: OrderEntity,

    /** Связь с товаром ([ProductEntity]). Идентификатор товара является частью ключа. */
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    val product: ProductEntity, // TODO может тут проекцию проставить вместо entity

    /** Количество товара в заказе. */
    @Column(nullable = false, precision = 15, scale = 2)
    var quantity: BigDecimal,

    /** Цена товара на момент оформления заказа. */
    @Column(nullable = false, precision = 15, scale = 2)
    var price: BigDecimal
)

/**
 * Составной идентификатор для [OrderProductEntity], состоящий из идентификаторов заказа и товара.
 *
 * Реализует интерфейс [Serializable] для корректной работы с JPA и Hibernate.
 */
@Embeddable
data class OrderProductId(
    /** Идентификатор заказа ([OrderEntity]). */
    @Column(name = "order_id")
    val orderId: UUID = UUID.randomUUID(),

    /** Идентификатор товара ([ProductEntity]). */
    @Column(name = "product_id")
    val productId: UUID = UUID.randomUUID()
) : java.io.Serializable
