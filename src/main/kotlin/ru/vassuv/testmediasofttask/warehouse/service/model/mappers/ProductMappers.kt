package ru.vassuv.testmediasofttask.warehouse.service.model.mappers

import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.service.model.ProductData

internal fun ProductEntity.toProductData(): ProductData = ProductData(
    id = requireNotNull(this.id) { "Id не должен быть null у существующего товара." },
    name = this.name,
    article = this.article,
    description = this.description,
    category = this.category,
    price = this.price,
    quantity = this.quantity,
    quantityUpdatedAt = this.quantityUpdatedAt,
    createdAt = this.createdAt,
    isAvailable = this.isAvailable
)

internal fun CreatedProduct.toProductEntity() = ProductEntity(
    name = name,
    article = article,
    description = description,
    category = category,
    price = price,
    quantity = quantity
)

