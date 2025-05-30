package ru.vassuv.testmediasofttask.warehouse.controller.mappers

import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.service.model.ProductData
import ru.vassuv.testmediasofttask.warehouse.service.model.UpdatedProduct
import ru.vassuv.testmediasofttask.warehouse.controller.request.CreateProductRequest
import ru.vassuv.testmediasofttask.warehouse.controller.response.ProductResponse
import ru.vassuv.testmediasofttask.warehouse.controller.request.UpdateProductRequest
import ru.vassuv.testmediasofttask.warehouse.controller.response.UuidResponse
import ru.vassuv.testmediasofttask.warehouse.enums.ProductCurrencyType
import java.util.UUID

internal fun ProductData.toProductResponse(
    defaultCurrencyType: ProductCurrencyType = ProductCurrencyType.RUB,
) = ProductResponse(
    id = this.id,
    name = this.name,
    article = this.article,
    description = this.description,
    category = this.category,
    price = this.price,
    quantity = this.quantity,
    quantityUpdatedAt = this.quantityUpdatedAt,
    createdAt = this.createdAt,
    currency = defaultCurrencyType
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
