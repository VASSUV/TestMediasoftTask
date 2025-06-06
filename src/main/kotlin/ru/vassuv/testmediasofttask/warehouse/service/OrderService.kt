package ru.vassuv.testmediasofttask.warehouse.service

import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus
import ru.vassuv.testmediasofttask.warehouse.service.model.*
import java.util.UUID

/**
 * Интерфейс сервиса управления заказами.
 *
 * Предоставляет бизнес-логику создания, изменения, отмены и управления статусами заказов.
 */
interface OrderService {

    /**
     * Создаёт заказ для указанного заказчика.
     *
     * @param customerId идентификатор заказчика.
     * @param createdOrder данные создаваемого заказа ([CreatedOrder]).
     * @return идентификатор созданного заказа.
     */
    fun createOrder(customerId: UUID, createdOrder: CreatedOrder): UUID

    /**
     * Обновляет существующий заказ.
     *
     * @param orderId идентификатор заказа.
     * @param updatedOrder обновлённые данные заказа ([UpdatedOrder]).
     */
    fun updateOrder(orderId: UUID, updatedOrder: UpdatedOrder)

    /**
     * Возвращает данные заказа по его идентификатору.
     *
     * @param orderId идентификатор заказа.
     * @return данные о заказе ([OrderData]).
     */
    fun getOrderById(orderId: UUID): OrderData

    /**
     * Отменяет заказ.
     *
     * @param orderId идентификатор заказа.
     */
    fun cancelOrder(orderId: UUID)

    /**
     * Обновляет статус заказа.
     *
     * @param orderId идентификатор заказа.
     * @param newStatus новый статус заказа ([OrderStatus]).
     */
    fun updateOrderStatus(orderId: UUID, newStatus: OrderStatus)
}