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
import ru.vassuv.testmediasofttask.warehouse.persist.entity.OrderEntity
import ru.vassuv.testmediasofttask.warehouse.persist.entity.OrderProductEntity
import ru.vassuv.testmediasofttask.warehouse.persist.repository.CustomerRepository
import ru.vassuv.testmediasofttask.warehouse.persist.repository.OrderRepository
import ru.vassuv.testmediasofttask.warehouse.persist.repository.ProductRepository
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedOrder
import ru.vassuv.testmediasofttask.warehouse.service.model.OrderData
import ru.vassuv.testmediasofttask.warehouse.service.model.OrderProductItem
import ru.vassuv.testmediasofttask.warehouse.service.model.UpdatedOrder
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*
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
    private val orderRepository: OrderRepository
): OrderService {

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
            ?: throw CustomerNotFoundException(customerId)

        if(!customer.isActive) {
            CustomerInactiveException(customerId)
        }

        val order = OrderEntity(
            customer = customer,
            status = OrderStatus.CREATED,
            deliveryAddress = createdOrder.deliveryAddress
        )

        val orderProducts = createdOrder.items.map { item ->

            val product = productRepository.findById(item.productId)
                .orElseThrow { ProductNotFoundException(item.productId) }

            if (!product.isAvailable) {
                throw ProductUnavailableException(item.productId, "Товар недоступен для заказа")
            }

            if (product.quantity < item.quantity) {
                throw ProductUnavailableException(item.productId, "Недостаточное количество товара на складе")
            }

            product.quantity -= item.quantity
            product.quantityUpdatedAt = ZonedDateTime.now()

            OrderProductEntity(
                order = order,
                product = product,
                quantity = item.quantity,
                price = product.price
            )
        }

        order.orderProducts as MutableList += orderProducts

        orderRepository.save(order)

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
     */
    @Transactional
    override fun updateOrder(orderId: UUID, updatedOrder: UpdatedOrder) {
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException(orderId) }

        if(order.status != OrderStatus.CREATED) {
            throw OrderUnavailableException(orderId)
        }

        order.deliveryAddress = updatedOrder.deliveryAddress

        val deletingOrderProducts = order.orderProducts.asSequence().map { it.product.id }.toMutableSet()
        updatedOrder.items.forEach { item ->
            deletingOrderProducts.remove(item.productId)
            val product = productRepository.findById(item.productId)
                .orElseThrow { ProductNotFoundException(item.productId) }


            val orderProduct = order.orderProducts.find { it.product.id == item.productId } ?: {
                val orderProductEntity = OrderProductEntity(
                    order = order,
                    product = product,
                    quantity = item.quantity,
                    price = product.price
                )

                if (!product.isAvailable) {
                    throw ProductUnavailableException(item.productId, "Товар недоступен для заказа")
                }

                if (product.quantity < item.quantity) {
                    throw ProductUnavailableException(item.productId, "Недостаточное количество товара на складе")
                }

                product.quantity -= item.quantity
                product.quantityUpdatedAt = ZonedDateTime.now()

                order.orderProducts as MutableList += orderProductEntity
                orderProductEntity
            }()

            val quantityDifference = item.quantity.subtract(orderProduct.quantity)

            if (!product.isAvailable) {
                throw ProductUnavailableException(orderId, "Товар недоступен для заказа")
            }

            if (quantityDifference > BigDecimal.ZERO && product.quantity < quantityDifference) {
                throw ProductUnavailableException(orderId, "Недостаточное количество товара на складе")
            }

            product.quantity = product.quantity.subtract(quantityDifference)
            product.quantityUpdatedAt = ZonedDateTime.now()

            orderProduct.quantity = item.quantity
            orderProduct.price = product.price
        }

        (order.orderProducts as MutableList).removeIf { orderProduct ->
            (orderProduct.product.id in deletingOrderProducts)
                .also { isDeleting ->
                    if(isDeleting) {
                        orderProduct.product.quantity += orderProduct.quantity
                    }
                }
        }

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

        if(order.status == OrderStatus.CANCELED) {
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
     */
    @Transactional
    override fun cancelOrder(orderId: UUID) {
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException(orderId) }

        if (order.status != OrderStatus.CREATED && order.status != OrderStatus.CONFIRMED ) {
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
    @Transactional
    override fun updateOrderStatus(orderId: UUID, newStatus: OrderStatus) {
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException(orderId) }

        if (!isStatusTransitionAllowed(order.status, newStatus)) {
            throw IllegalOrderStatusException(
                orderId,
                order.status,
                "Переход в статус $newStatus запрещён"
            )
        }

        order.status = newStatus
        orderRepository.save(order)
    }

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