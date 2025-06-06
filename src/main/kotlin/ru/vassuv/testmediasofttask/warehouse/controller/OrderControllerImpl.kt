package ru.vassuv.testmediasofttask.warehouse.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toCreatedOrder
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toOrderResponse
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toProductOrderReportInfoResponse
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toUpdatedOrder
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
import ru.vassuv.testmediasofttask.warehouse.service.OrderService
import ru.vassuv.testmediasofttask.warehouse.service.model.ProductOrderReportInfo
import java.util.*

/**
 * Реализация REST-контроллера управления заказами.
 *
 * Отвечает за создание, изменение, отмену и просмотр заказов.
 */
@RestController
@RequestMapping("/api/order")
class OrderControllerImpl(
    /** Сервис управления заказами ([OrderService]). */
    private val orderService: OrderService
) : OrderController {

    /**
     * Создаёт заказ для указанного заказчика.
     *
     * Товары резервируются со склада, уменьшая доступное количество.
     *
     * @param customerId идентификатор заказчика, передаётся в заголовке запроса.
     * @param request данные нового заказа ([CreateOrderRequest]).
     * @return Ответ с идентификатором заказа ([UuidResponse]) и статусом CREATED.
     *
     * @throws ProductNotFoundException если товар не найден.
     * @throws ProductUnavailableException если товара недостаточно или он недоступен.
     * @throws CustomerNotFoundException если заказчик не найден.
     * @throws CustomerInactiveException если заказчик неактивен.
     */
    @PostMapping
    override fun createOrder(
        @RequestHeader("customerId") customerId: UUID,
        @Valid @RequestBody request: CreateOrderRequest
    ): ResponseEntity<UuidResponse> {
        val orderId = orderService.createOrder(customerId, request.toCreatedOrder())
        return ResponseEntity.status(HttpStatus.CREATED).body(UuidResponse(orderId))
    }

    /**
     * Обновляет позиции существующего заказа.
     *
     * Перераспределяет товары с учётом новых данных и резервирует их на складе.
     *
     * @param customerId идентификатор заказчика, передаётся в заголовке запроса.
     * @param orderId идентификатор заказа.
     * @param request новые данные для обновления ([UpdateOrderRequest]).
     * @return HTTP-ответ со статусом NO_CONTENT.
     *
     * @throws OrderNotFoundException если заказ не найден.
     * @throws IllegalOrderStatusException если статус заказа не позволяет изменений.
     * @throws ProductUnavailableException если товаров недостаточно на складе.
     * @throws OrderUnavailableException если заказ недоступен.
     * @throws CustomerNotFoundException если заказчик не найден.
     * @throws CustomerInactiveException если заказчик неактивен.
     */
    @PatchMapping("/{orderId}")
    override fun updateOrder(
        @RequestHeader("customerId") customerId: UUID,
        @PathVariable orderId: UUID,
        @Valid @RequestBody request: UpdateOrderRequest
    ): ResponseEntity<Void> {
        orderService.updateOrder(customerId, orderId, request.toUpdatedOrder())
        return ResponseEntity.noContent().build()
    }

    /**
     * Получает информацию о заказе.
     *
     * @param orderId идентификатор заказа.
     * @return Ответ с деталями заказа ([OrderResponse]).
     *
     * @throws OrderNotFoundException если заказ не найден.
     * @throws OrderUnavailableException если заказ недоступен.
     */
    @GetMapping("/{orderId}")
    override fun getOrder(@PathVariable orderId: UUID): ResponseEntity<OrderResponse> {
        val order = orderService.getOrderById(orderId).toOrderResponse()
        return ResponseEntity.ok(order)
    }

    /**
     * Отменяет заказ, возвращая товары обратно на склад.
     *
     * Меняет статус заказа на [OrderStatus.CANCELED].
     *
     * @param customerId идентификатор заказчика, передаётся в заголовке запроса.
     * @param orderId идентификатор заказа.
     * @return HTTP-ответ со статусом NO_CONTENT.
     *
     * @throws OrderNotFoundException если заказ не найден.
     * @throws IllegalOrderStatusException если статус заказа не позволяет отмену.
     * @throws CustomerNotFoundException если заказчик не найден.
     * @throws CustomerInactiveException если заказчик неактивен.
     */
    @DeleteMapping("/{orderId}")
    override fun cancelOrder(
        @RequestHeader("customerId") customerId: UUID,
        @PathVariable orderId: UUID
    ): ResponseEntity<Void> {
        orderService.cancelOrder(customerId, orderId)
        return ResponseEntity.noContent().build()
    }

    /**
     * Подтверждение заказа (реализация отложена на будущее).
     *
     * @param orderId идентификатор заказа.
     * @return HTTP-ответ со статусом NOT_IMPLEMENTED.
     */
    @Suppress("ForbiddenComment")
    @PostMapping("/{orderId}/confirm")
    override fun confirmOrder(@PathVariable orderId: UUID): ResponseEntity<Void> {
        // TODO: Реализовать подтверждение заказа
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build()
    }

    /**
     * Обновляет статус заказа.
     *
     * Используется для внутреннего управления состоянием заказов.
     *
     * @param orderId идентификатор заказа.
     * @param request новый статус заказа ([UpdateOrderStatusRequest]).
     * @return HTTP-ответ со статусом NO_CONTENT.
     *
     * @throws OrderNotFoundException если заказ не найден.
     * @throws IllegalOrderStatusException если статус не может быть изменён.
     */
    @PatchMapping("/{orderId}/status")
    override fun updateOrderStatus(
        @PathVariable orderId: UUID,
        @Valid @RequestBody request: UpdateOrderStatusRequest
    ): ResponseEntity<Void> {
        orderService.updateOrderStatus(orderId, request.status)
        return ResponseEntity.noContent().build()
    }

    /**
     * Возвращает отчет по продуктам и заказам с этими продуктами.
     *
     * @return Сформированный отчет по продуктам и заказам.
     */
    @GetMapping("/report/product-orders")
    override fun getProductOrderReport(): Map<UUID, List<ProductOrderReportInfoResponse>>{
        return orderService.getProductOrderReport().mapValues { (_, reports) ->
            reports.map { it.toProductOrderReportInfoResponse() }
        }
    }
}
