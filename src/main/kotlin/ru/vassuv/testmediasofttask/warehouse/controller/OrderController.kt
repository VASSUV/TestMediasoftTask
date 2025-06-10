package ru.vassuv.testmediasofttask.warehouse.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import ru.vassuv.testmediasofttask.warehouse.config.security.WarehousePrincipal
import ru.vassuv.testmediasofttask.warehouse.controller.request.CreateOrderRequest
import ru.vassuv.testmediasofttask.warehouse.controller.request.UpdateOrderRequest
import ru.vassuv.testmediasofttask.warehouse.controller.request.UpdateOrderStatusRequest
import ru.vassuv.testmediasofttask.warehouse.controller.response.OrderResponse
import ru.vassuv.testmediasofttask.warehouse.controller.response.ProductOrderReportInfoResponse
import ru.vassuv.testmediasofttask.warehouse.controller.response.UuidResponse
import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus
import ru.vassuv.testmediasofttask.warehouse.exception.CustomerInactiveException
import ru.vassuv.testmediasofttask.warehouse.exception.CustomerNotFoundException
import ru.vassuv.testmediasofttask.warehouse.exception.IllegalOrderStatusException
import ru.vassuv.testmediasofttask.warehouse.exception.OrderNotFoundException
import ru.vassuv.testmediasofttask.warehouse.exception.OrderUnavailableException
import ru.vassuv.testmediasofttask.warehouse.exception.ProductNotFoundException
import ru.vassuv.testmediasofttask.warehouse.exception.ProductUnavailableException
import ru.vassuv.testmediasofttask.warehouse.persist.entity.CustomerEntity
import ru.vassuv.testmediasofttask.warehouse.persist.entity.OrderEntity
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import java.util.*

/**
 * Интерфейс REST-контроллера для управления заказами.
 *
 * Предоставляет методы для создания, обновления, отмены и просмотра заказов.
 */
@SecurityRequirement(name = "BearerAuth")
@Tag(
    name = "Orders",
    description = "Управление заказами"
)
interface OrderController {

    /**
     * Создаёт заказ для указанного заказчика ([CustomerEntity]).
     *
     * Товары резервируются со склада ([ProductEntity]), уменьшая доступное количество.
     *
     * @param principal параметры из аутентификации ([WarehousePrincipal]).
     * @param request данные нового заказа ([CreateOrderRequest]).
     * @return идентификатор созданного заказа ([UuidResponse]).
     *
     * @throws ProductNotFoundException если товар не найден.
     * @throws ProductUnavailableException если товара недостаточно или он недоступен.
     * @throws CustomerNotFoundException если заказчик не найден.
     * @throws CustomerInactiveException если заказчик неактивен.
     */
    @Operation(
        summary = "Создание заказа",
        description = "Создаёт заказ для указанного заказчика с резервированием товаров на складе."
    )
    fun createOrder(principal: WarehousePrincipal, request: CreateOrderRequest): ResponseEntity<UuidResponse>

    /**
     * Обновляет позиции существующего заказа ([OrderEntity]).
     *
     * Товары перераспределяются с учётом новых данных, обновляется резерв на складе.
     *
     * @param principal параметры из аутентификации ([WarehousePrincipal]).
     * @param orderId идентификатор заказа.
     * @param request обновлённые позиции заказа ([UpdateOrderRequest]).
     * @return HTTP-ответ со статусом NO_CONTENT.
     *
     * @throws OrderNotFoundException если заказ не найден.
     * @throws IllegalOrderStatusException если статус заказа не допускает изменения.
     * @throws ProductUnavailableException если товара недостаточно на складе.
     * @throws OrderUnavailableException если заказ недоступен.
     */
    @Operation(
        summary = "Обновление заказа",
        description = "Обновляет позиции заказа с проверкой наличия товара и корректности статуса."
    )
    fun updateOrder(principal: WarehousePrincipal, orderId: UUID, request: UpdateOrderRequest): ResponseEntity<Void>

    /**
     * Возвращает информацию о заказе по идентификатору.
     *
     * @param orderId идентификатор заказа.
     * @return Информация о заказе и его позициях ([OrderResponse]).
     *
     * @throws OrderNotFoundException если заказ не найден.
     * @throws OrderUnavailableException если заказ недоступен.
     */
    @Operation(
        summary = "Получение заказа",
        description = "Возвращает информацию о заказе по указанному идентификатору."
    )
    fun getOrder(orderId: UUID): ResponseEntity<OrderResponse>

    /**
     * Отменяет заказ и возвращает зарезервированные товары на склад.
     *
     * Меняет статус заказа на [OrderStatus.CANCELED].
     *
     * @param principal параметры из аутентификации ([WarehousePrincipal]).
     * @param orderId идентификатор заказа для отмены.
     * @return HTTP-ответ со статусом NO_CONTENT.
     *
     * @throws OrderNotFoundException если заказ не найден.
     * @throws IllegalOrderStatusException если текущий статус заказа не позволяет его отменить.
     */
    @Operation(
        summary = "Отмена заказа",
        description = "Отменяет заказ и возвращает зарезервированные товары обратно на склад."
    )
    fun cancelOrder(principal: WarehousePrincipal, orderId: UUID): ResponseEntity<Void>

    /**
     * Подтверждение заказа (реализация будет позже).
     *
     * @param orderId идентификатор заказа.
     * @return HTTP-ответ со статусом NO_CONTENT.
     */
    @Operation(
        summary = "Подтверждение заказа",
        description = "Подтверждает заказ, реализация функционала запланирована на будущее."
    )
    fun confirmOrder(orderId: UUID): ResponseEntity<Void>

    /**
     * Обновляет статус заказа (используется внутренними процессами).
     *
     * @param orderId идентификатор заказа.
     * @param request новый статус заказа ([UpdateOrderStatusRequest]).
     * @return HTTP-ответ со статусом NO_CONTENT.
     *
     * @throws OrderNotFoundException если заказ не найден.
     * @throws IllegalOrderStatusException при некорректной попытке изменения статуса.
     */
    @Operation(
        summary = "Обновление статуса заказа",
        description = "Изменяет статус заказа на указанный, с соблюдением правил перехода состояний."
    )
    fun updateOrderStatus(orderId: UUID, request: UpdateOrderStatusRequest): ResponseEntity<Void>

    /**
     * Возвращает отчет по продуктам и заказам с этими продуктами.
     *
     * @return Сформированный отчет по продуктам и заказам.
     */
    @Operation(
        summary = "Формирование отчета по продуктам и заказам с этими продуктами",
        description = "Формирует отчет по продуктам и заказам с этими продуктами"
    )
    @GetMapping("/api/report/product-orders")
    fun getProductOrderReport(): Map<UUID, List<ProductOrderReportInfoResponse>>
}
