package ru.vassuv.testmediasofttask.warehouse.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus
import ru.vassuv.testmediasofttask.warehouse.exception.CustomerInactiveException
import ru.vassuv.testmediasofttask.warehouse.exception.CustomerNotFoundException
import ru.vassuv.testmediasofttask.warehouse.exception.IllegalOrderStatusException
import ru.vassuv.testmediasofttask.warehouse.exception.OrderNotFoundException
import ru.vassuv.testmediasofttask.warehouse.exception.OrderUnavailableException
import ru.vassuv.testmediasofttask.warehouse.exception.ProductNotFoundException
import ru.vassuv.testmediasofttask.warehouse.exception.ProductUnavailableException
import ru.vassuv.testmediasofttask.warehouse.exception.checkCustomerInactive
import ru.vassuv.testmediasofttask.warehouse.exception.checkProductUnavailable
import ru.vassuv.testmediasofttask.warehouse.exception.customerNotFoundError
import ru.vassuv.testmediasofttask.warehouse.exception.productNotFoundError
import ru.vassuv.testmediasofttask.warehouse.exception.productOrderReportError
import ru.vassuv.testmediasofttask.warehouse.persist.entity.OrderEntity
import ru.vassuv.testmediasofttask.warehouse.persist.entity.OrderProductEntity
import ru.vassuv.testmediasofttask.warehouse.persist.repository.CustomerRepository
import ru.vassuv.testmediasofttask.warehouse.persist.repository.OrderRepository
import ru.vassuv.testmediasofttask.warehouse.persist.repository.ProductRepository
import ru.vassuv.testmediasofttask.warehouse.service.event.KafkaEvent
import ru.vassuv.testmediasofttask.warehouse.service.event.KafkaTopic
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedOrder
import ru.vassuv.testmediasofttask.warehouse.service.model.CustomerReportInfo
import ru.vassuv.testmediasofttask.warehouse.service.model.OrderData
import ru.vassuv.testmediasofttask.warehouse.service.model.OrderProductItem
import ru.vassuv.testmediasofttask.warehouse.service.model.ProductOrderReportInfo
import ru.vassuv.testmediasofttask.warehouse.service.model.UpdatedOrder
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.jvm.optionals.getOrNull

/**
 * Реализация сервиса управления заказами ([OrderService]).
 *
 * Содержит логику создания, изменения, отмены заказов и управления их статусами.
 */
@Service
class OrderServiceImpl(
    private val customerRepository: CustomerRepository,
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val customerDataService: CustomerDataService,
    private val kafkaProducerService: KafkaProducerService
) : OrderService {

    /**
     * Создание заказа.
     *
     * @param customerId идентификатор заказчика.
     * @param createdOrder данные создания заказа.
     *
     * @throws ProductNotFoundException если товар не найден.
     * @throws ProductUnavailableException если товара недостаточно или он недоступен.
     * @throws CustomerNotFoundException если заказчик не найден.
     * @throws CustomerInactiveException если заказчик неактивен.
     */
    @Transactional
    override fun createOrder(customerId: UUID, createdOrder: CreatedOrder): UUID {
        val customer = customerRepository.findById(customerId).getOrNull()
            ?: customerNotFoundError(customerId)

        customer.checkCustomerInactive()

        val order = OrderEntity(
            customer = customer,
            status = OrderStatus.CREATED,
            deliveryAddress = createdOrder.deliveryAddress
        )

        val createdOrderProductsMap = createdOrder.items.associateBy { it.productId }
        val products = productRepository.findAllById(createdOrderProductsMap.keys)

        if (products.size != createdOrderProductsMap.size) {
            val notFoundedProducts = createdOrderProductsMap.keys.subtract(products.map { it.id!! })
            productNotFoundError(notFoundedProducts.first())
        }

        val orderProducts = products
            .map { product ->
                val productId = product.id!!
                val createdOrderItem = createdOrderProductsMap[productId]
                    ?: productNotFoundError(productId)
                product.checkProductUnavailable(createdOrderItem.quantity)

                product.quantity -= createdOrderItem.quantity
                product.quantityUpdatedAt = ZonedDateTime.now()

                OrderProductEntity(
                    order = order,
                    product = product,
                    quantity = createdOrderItem.quantity,
                    price = product.price
                )
            }

        order.orderProducts as MutableList += orderProducts

        orderRepository.save(order)

        kafkaProducerService.sendEvent(
            topic = KafkaTopic.WAREHOUSE,
            event = KafkaEvent.Order.Create(
                customerId = customerId,
                deliveryAddress = order.deliveryAddress,
                products = order.orderProducts.map {
                    KafkaEvent.Order.Create.Product(
                        id = it.id.productId,
                        quantity = it.quantity
                    )
                }
            ),
            key = "create order ${order.id!!}"
        )

        return order.id!!
    }

    /**
     * Изменение существующего заказа.
     *
     * @param orderId идентификатор заказа.
     * @param updatedOrder данные изменения заказа.
     *
     * @throws ProductNotFoundException если товар не найден.
     * @throws ProductUnavailableException если товара недостаточно или он недоступен.
     * @throws OrderNotFoundException если заказ не найден.
     * @throws OrderUnavailableException если заказ недоступен.
     * @throws CustomerNotFoundException если заказчик не найден.
     * @throws CustomerInactiveException если заказчик неактивен.
     */
    @Transactional
    override fun updateOrder(customerId: UUID, orderId: UUID, updatedOrder: UpdatedOrder) {

        val customer = customerRepository.findById(customerId).getOrNull()
            ?: customerNotFoundError(customerId)

        customer.checkCustomerInactive()

        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException(orderId) }

        if (order.status != OrderStatus.CREATED) {
            throw OrderUnavailableException(orderId)
        }

        order.deliveryAddress = updatedOrder.deliveryAddress

        val updatingProductItems = updatedOrder.items.associateBy { it.productId }
        val existedProductsMap = order.orderProducts.associateBy { it.id.productId }

        val (updatedIds, deletedIds) = existedProductsMap.keys
            .partition { it in updatingProductItems.keys }
            .let { it.first.toSet() to it.second.toSet() }

        val allUpdatedProductIds = updatingProductItems.keys + deletedIds
        val products = productRepository.findAllById(allUpdatedProductIds)

        products.forEach { product ->
            val productId = product.id!!

            when (productId) {
                in deletedIds -> {
                    val item = existedProductsMap[productId]
                    if (item != null) {
                        product.quantity += item.quantity
                        product.quantityUpdatedAt = ZonedDateTime.now()
                        (order.orderProducts as MutableList).remove(item)
                    }
                }

                in updatedIds -> {
                    val existingItem = existedProductsMap[productId]
                    val updatingItem = updatingProductItems[productId]
                    if (existingItem != null && updatingItem != null) {
                        val quantityDifference = updatingItem.quantity - existingItem.quantity
                        product.checkProductUnavailable(quantityDifference)

                        product.quantity -= quantityDifference
                        product.quantityUpdatedAt = ZonedDateTime.now()
                        existingItem.quantity = updatingProductItems[productId]!!.quantity
                        existingItem.price = product.price
                    }
                }

                else -> {
                    val creatingProductItem = OrderProductEntity(
                        order = order,
                        product = product,
                        quantity = updatingProductItems[productId]!!.quantity,
                        price = product.price
                    )
                    (order.orderProducts as MutableList).add(creatingProductItem)
                    product.checkProductUnavailable(creatingProductItem.quantity)
                    product.quantity -= creatingProductItem.quantity
                    product.quantityUpdatedAt = ZonedDateTime.now()
                }
            }
        }

        kafkaProducerService.sendEvent(
            topic = KafkaTopic.WAREHOUSE,
            event = KafkaEvent.Order.Update(
                orderId = orderId,
                customerId = customerId,
                products = order.orderProducts.map {
                    KafkaEvent.Order.Update.Product(
                        id = it.id.productId,
                        quantity = it.quantity
                    )
                }
            ),
            key = "update order ${order.id!!}"
        )

        orderRepository.save(order)
    }

    /**
     * Получает данные заказа по его идентификатору.
     *
     * @param orderId идентификатор заказа.
     * @return информация о заказе с продуктами.
     *
     * @throws OrderNotFoundException если заказ не найден.
     * @throws OrderUnavailableException если заказ недоступен.
     */
    @Transactional(readOnly = true)
    override fun getOrderById(orderId: UUID): OrderData {
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException(orderId) }

        if (order.status == OrderStatus.CANCELED) {
            throw OrderUnavailableException(orderId)
        }

        var totalPrice = 0.toBigDecimal()
        val products = order.orderProducts.map { orderProduct ->
            totalPrice = totalPrice.add(orderProduct.price.multiply(orderProduct.quantity))
            OrderProductItem(
                id = orderProduct.product.id!!,
                name = orderProduct.product.name,
                quantity = orderProduct.quantity,
                price = orderProduct.price
            )
        }

        return OrderData(
            id = orderId,
            customerId = order.customer.id!!,
            status = order.status,
            deliveryAddress = order.deliveryAddress,
            totalPrice = totalPrice,
            products = products
        )
    }

    /**
     * Отменяет заказ, переводя его в статус CANCELED.
     * Возвращает товар обратно на склад.
     *
     * @param orderId идентификатор отменяемого заказа.
     *
     * @throws OrderNotFoundException если заказ не найден.
     * @throws IllegalOrderStatusException если статус заказа не допускает отмены.
     * @throws CustomerNotFoundException если заказчик не найден.
     * @throws CustomerInactiveException если заказчик неактивен.
     */
    @Transactional
    override fun cancelOrder(customerId: UUID, orderId: UUID) {
        val customer = customerRepository.findById(customerId).getOrNull()
            ?: customerNotFoundError(customerId)

        customer.checkCustomerInactive()

        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException(orderId) }

        if (order.status != OrderStatus.CREATED && order.status != OrderStatus.CONFIRMED) {
            throw IllegalOrderStatusException(
                orderId,
                order.status,
                "Заказ нельзя отменить в текущем статусе"
            )
        }

        order.status = OrderStatus.CANCELED

        order.orderProducts.forEach { orderProduct ->
            val product = orderProduct.product
            product.quantity = product.quantity.add(orderProduct.quantity)
            product.quantityUpdatedAt = ZonedDateTime.now()
        }

        kafkaProducerService.sendEvent(
            topic = KafkaTopic.WAREHOUSE,
            event = KafkaEvent.Order.Delete(
                orderId = orderId,
                customerId = customerId,
            ),
            key = "cancel order ${order.id!!}"
        )

        orderRepository.save(order)
    }

    /**
     * Изменение статуса заказа с проверкой допустимости перехода.
     *
     * Диаграмма переходов
     *
     * CREATED → CONFIRMED → DONE
     *     ↓          ↓
     * REJECTED   CANCELED
     *
     * @param orderId идентификатор заказа.
     * @param newStatus новый статус заказа.
     *
     * @throws OrderNotFoundException если заказ не найден.
     * @throws IllegalOrderStatusException если переход между статусами запрещён.
     */
    @Suppress("UseCheckOrError")
    @Transactional
    override fun updateOrderStatus(orderId: UUID, newStatus: OrderStatus) {
        val currentStatus = orderRepository.findStatusById(orderId)
            ?: throw OrderNotFoundException(orderId)

        if (!isStatusTransitionAllowed(currentStatus, newStatus)) {
            throw IllegalOrderStatusException(
                orderId,
                currentStatus,
                "Переход в статус $newStatus запрещён"
            )
        }

        val updatedRows = orderRepository.updateOrderStatus(orderId, newStatus)
        if (updatedRows == 0) {
            throw IllegalStateException("Не удалось обновить статус заказа $orderId")
        }

        kafkaProducerService.sendEvent(
            topic = KafkaTopic.WAREHOUSE,
            event = KafkaEvent.Order.UpdateStatus(
                orderId = orderId,
                status = newStatus,
            ),
            key = "change order status $orderId"
        )
    }

    /**
     * Возвращает отчет по продуктам и заказам с этими продуктами.
     *
     * @return Отчет о продуктах и заказах([ProductOrderReportInfo]).
     */
    override fun getProductOrderReport(): Map<UUID, List<ProductOrderReportInfo>> {
        val statuses = setOf(OrderStatus.CREATED, OrderStatus.CONFIRMED)
        val orders = orderRepository.findAllWithProductsByStatuses(statuses)
        val logins = orders.asSequence().map { it.customer }.distinctBy { it.id }.map { it.login }.toSet()

        val innFuture = CompletableFuture
            .supplyAsync { customerDataService.getInns(logins) }
            .exceptionally { ex ->
                productOrderReportError("Ошибка при получении ИНН: ${ex.message}", ex)
            }
        val accFuture = CompletableFuture
            .supplyAsync { customerDataService.getAccountNumbers(logins) }
            .exceptionally { ex ->
                productOrderReportError("Ошибка при получении номера счета: ${ex.message}", ex)
            }

        return buildProductOrderReport(orders, innFuture.join(), accFuture.join())
    }

    /**
     * Метод сборки отчета по продуктам и заказам с этими продуктами
     *
     * @param orders Все заказы
     * @param innMap Набор инн по всем заказчикам
     * @param accMap Набор номеров счетов по всем заказчикам
     * @return Сгруппированный по продуктам отчет по заказам
     */
    private fun buildProductOrderReport(
        orders: List<OrderEntity>,
        innMap: Map<String, String>,
        accMap: Map<String, String>
    ): Map<UUID, List<ProductOrderReportInfo>> = orders.asSequence()
        .flatMap { order ->
            order.orderProducts
                .mapNotNull { product ->
                    val orderId = order.id
                    val customerId = order.customer.id
                    if (orderId != null && customerId != null) {
                        val report = ProductOrderReportInfo(
                            orderId = orderId,
                            customer = CustomerReportInfo(
                                id = customerId,
                                login = order.customer.login,
                                email = order.customer.email,
                                inn = innMap.getValue(order.customer.login),
                                accountNumber = accMap.getValue(order.customer.login),
                            ),
                            status = order.status,
                            deliveryAddress = order.deliveryAddress,
                            quantity = product.quantity,
                        )
                        report to product.id.productId
                    } else {
                        null
                    }
                }
        }
        .groupBy(
            keySelector = { it.second }, // productId
            valueTransform = { it.first } // ProductOrderReportInfo
        )

    /**
     * Проверяет допустимость перехода из текущего статуса в новый.
     *
     * @param current текущий статус заказа.
     * @param new новый статус заказа.
     * @return true, если переход допустим; иначе false.
     */
    private fun isStatusTransitionAllowed(current: OrderStatus, new: OrderStatus): Boolean {
        return when (current) {
            OrderStatus.CREATED -> new in setOf(OrderStatus.CONFIRMED, OrderStatus.CANCELED, OrderStatus.REJECTED)
            OrderStatus.CONFIRMED -> new in setOf(OrderStatus.DONE, OrderStatus.CANCELED)
            OrderStatus.DONE, OrderStatus.CANCELED, OrderStatus.REJECTED -> false
        }
    }
}
