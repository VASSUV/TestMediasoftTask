package ru.vassuv.testmediasofttask.warehouse.persist.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus
import java.util.*

/**
 * Сущность заказа ([OrderEntity]) для хранения информации о заказах, сделанных клиентами.
 *
 * Связывает товары ([ProductEntity]) и заказчиков ([CustomerEntity])
 * через промежуточную сущность ([OrderProductEntity]).
 */
@Entity
@Table(name = "\"order\"")
class OrderEntity(
    /** Уникальный идентификатор заказа. Генерируется автоматически. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    /** Заказчик, оформивший данный заказ. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    val customer: CustomerEntity,

    /** Текущий статус заказа ([OrderStatus]). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus,

    /** Адрес доставки заказа. */
    @Column(nullable = false)
    var deliveryAddress: String,

    /** Список позиций в заказе. Связь с товарами и их количеством через [OrderProductEntity]. */
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderProducts: List<OrderProductEntity> = mutableListOf(),

    /** Бизнес ключ процесса заказа */
    @Column
    var businessKey: String? = null
)
