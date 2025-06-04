package ru.vassuv.testmediasofttask.warehouse.persist.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus
import ru.vassuv.testmediasofttask.warehouse.persist.entity.OrderEntity
import java.util.*

/**
 * Репозиторий для работы с заказами ([OrderEntity]) в базе данных.
 *
 * Наследует стандартные методы CRUD-операций из [JpaRepository].
 * Используется для управления жизненным циклом заказов и их статусами.
 */
interface OrderRepository : JpaRepository<OrderEntity, UUID> {

    @Modifying
    @Query("UPDATE OrderEntity o SET o.status = :newStatus WHERE o.id = :orderId")
    fun updateOrderStatus(orderId: UUID, newStatus: OrderStatus): Int

    @Query("SELECT o.status FROM OrderEntity o WHERE o.id = :orderId")
    fun findStatusById(orderId: UUID): OrderStatus?

    @Query("""
        SELECT o FROM OrderEntity o
        JOIN FETCH o.customer c
        LEFT JOIN FETCH o.orderProducts op
        LEFT JOIN FETCH op.product p
        WHERE o.status IN :statuses
    """
    )
    fun findAllWithProductsByStatuses(@Param("statuses") statuses: Set<OrderStatus>): List<OrderEntity>
}