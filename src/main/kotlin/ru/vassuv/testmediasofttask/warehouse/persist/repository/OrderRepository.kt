package ru.vassuv.testmediasofttask.warehouse.persist.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.vassuv.testmediasofttask.warehouse.persist.entity.OrderEntity
import java.util.*

/**
 * Репозиторий для работы с заказами ([OrderEntity]) в базе данных.
 *
 * Наследует стандартные методы CRUD-операций из [JpaRepository].
 * Используется для управления жизненным циклом заказов и их статусами.
 */
interface OrderRepository : JpaRepository<OrderEntity, UUID> {
    // Дополнительные кастомные методы можно добавлять здесь по мере необходимости
}