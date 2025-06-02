package ru.vassuv.testmediasofttask.warehouse.persist.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

/**
 * Сущность заказчика ([CustomerEntity]) для хранения информации о клиентах.
 *
 * Используется для идентификации заказчиков и управления их состоянием в системе.
 */
@Entity
@Table(name = "customer")
class CustomerEntity(
    /** Уникальный идентификатор заказчика. Генерируется автоматически. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    /** Уникальный логин заказчика для входа в систему. */
    @Column(nullable = false, unique = true)
    var login: String,

    /** Уникальный адрес электронной почты заказчика. */
    @Column(nullable = false, unique = true)
    var email: String,

    /** Флаг активности заказчика. Неактивные заказчики не могут создавать заказы. */
    @Column(nullable = false)
    var isActive: Boolean = true
)