package ru.vassuv.testmediasofttask.warehouse.service

import ru.vassuv.testmediasofttask.warehouse.config.security.WarehousePrincipal
import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedOrder
import ru.vassuv.testmediasofttask.warehouse.service.model.OrderData
import ru.vassuv.testmediasofttask.warehouse.service.model.ProductOrderReportInfo
import ru.vassuv.testmediasofttask.warehouse.service.model.UpdatedOrder
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
     * @param customerProfileId Идентификатор профиля заказчика
     * @param createdOrder данные создаваемого заказа ([CreatedOrder]).
     * @return идентификатор созданного заказа.
     */
    fun createOrder(customerProfileId: UUID, createdOrder: CreatedOrder): UUID

    /**
     * Обновляет существующий заказ.
     *
     * @param customerProfileId Идентификатор профиля заказчика
     * @param orderId идентификатор заказа.
     * @param updatedOrder обновлённые данные заказа ([UpdatedOrder]).
     */
    fun updateOrder(customerProfileId: UUID, orderId: UUID, updatedOrder: UpdatedOrder)

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
     * @param customerProfileId Идентификатор профиля заказчика
     * @param orderId идентификатор заказа.
     */
    fun cancelOrder(customerProfileId: UUID, orderId: UUID)

    /**
     * Обновляет статус заказа.
     *
     * @param orderId идентификатор заказа.
     * @param newStatus новый статус заказа ([OrderStatus]).
     */
    fun updateOrderStatus(orderId: UUID, newStatus: OrderStatus)

    /**
     * Возвращает отчет по продуктам и заказам с этими продуктами.
     *
     * @return Отчет о продуктах и заказах([ProductOrderReportInfo]).
     */
    fun getProductOrderReport(): Map<UUID, List<ProductOrderReportInfo>>
}
