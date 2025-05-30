package ru.vassuv.testmediasofttask.warehouse.controller.mappers

import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.service.model.ProductData
import ru.vassuv.testmediasofttask.warehouse.service.model.UpdatedProduct
import ru.vassuv.testmediasofttask.warehouse.controller.request.CreateProductRequest
import ru.vassuv.testmediasofttask.warehouse.controller.response.ProductResponse
import ru.vassuv.testmediasofttask.warehouse.controller.request.UpdateProductRequest
import ru.vassuv.testmediasofttask.warehouse.controller.response.UuidResponse
import ru.vassuv.testmediasofttask.warehouse.service.model.ExchangeRateInfo
import java.math.BigDecimal
import java.util.UUID

internal fun ProductData.toProductResponse(
    exchangeRateInfo: ExchangeRateInfo,
) = ProductResponse(
    id = this.id,
    name = this.name,
    article = this.article,
    description = this.description,
    category = this.category,
    price = this.price.convertPrice(exchangeRateInfo),
    quantity = this.quantity,
    quantityUpdatedAt = this.quantityUpdatedAt,
    createdAt = this.createdAt,
    currency = exchangeRateInfo.currencyType
)

internal fun CreateProductRequest.toCreatedProduct() = CreatedProduct(
    name = name,
    article = article,
    description = description,
    category = category,
    price = price,
    quantity = quantity
)

internal fun UpdateProductRequest.toUpdatedProduct() = UpdatedProduct(
    name = name,
    article = article,
    description = description,
    category = category,
    price = price,
    quantity = quantity
)

internal fun UUID.toUuidResponse() = UuidResponse (this)

private fun BigDecimal.convertPrice(info: ExchangeRateInfo) = multiply(info.convertValue)
