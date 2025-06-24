package ru.vassuv.testmediasofttask.warehouse.controller.mappers

import ru.vassuv.testmediasofttask.warehouse.controller.request.CreateOrderItemRequest
import ru.vassuv.testmediasofttask.warehouse.controller.request.CreateOrderRequest
import ru.vassuv.testmediasofttask.warehouse.controller.request.UpdateOrderItemRequest
import ru.vassuv.testmediasofttask.warehouse.controller.request.UpdateOrderRequest
import ru.vassuv.testmediasofttask.warehouse.controller.response.ConfirmOrderResponse
import ru.vassuv.testmediasofttask.warehouse.controller.response.OrderProductItemResponse
import ru.vassuv.testmediasofttask.warehouse.controller.response.OrderResponse
import ru.vassuv.testmediasofttask.warehouse.service.model.ConfirmOrderResult
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedOrder
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedOrderItem
import ru.vassuv.testmediasofttask.warehouse.service.model.OrderData
import ru.vassuv.testmediasofttask.warehouse.service.model.OrderProductItem
import ru.vassuv.testmediasofttask.warehouse.service.model.UpdatedOrder
import ru.vassuv.testmediasofttask.warehouse.service.model.UpdatedOrderItem

/**
 * Мапперы для преобразования моделей, запросов и ответов, связанных с заказами.
 */

internal fun CreateOrderRequest.toCreatedOrder() = CreatedOrder(
    deliveryAddress = this.deliveryAddress,
    items = this.items.map { it.toCreatedOrderItem() }
)

private fun CreateOrderItemRequest.toCreatedOrderItem() = CreatedOrderItem(
    productId = productId,
    quantity = this.quantity,
)

internal fun UpdateOrderRequest.toUpdatedOrder() = UpdatedOrder(
    deliveryAddress = this.deliveryAddress,
    items = this.items.map { it.toUpdatedOrderItem() }
)

private fun UpdateOrderItemRequest.toUpdatedOrderItem() = UpdatedOrderItem(
    productId = this.productId,
    quantity = this.quantity,
)

internal fun OrderData.toOrderResponse() = OrderResponse (
    id = this.id,
    customerId = this.customerId,
    status = this.status,
    deliveryAddress = this.deliveryAddress,
    totalPrice = this.totalPrice,
    products = this.products.map { it.toOrderProductItemResponse() }
)

private fun OrderProductItem.toOrderProductItemResponse() = OrderProductItemResponse (
     id = this.id,
     name = this.name,
     quantity = this.quantity,
     price = this.price,
)


fun ConfirmOrderResult.toConfirmOrderResponse() = ConfirmOrderResponse(
    businessKey = this.businessKey,
    status = this.status,
)
