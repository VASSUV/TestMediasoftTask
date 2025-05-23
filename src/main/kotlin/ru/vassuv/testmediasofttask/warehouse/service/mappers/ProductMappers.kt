package ru.vassuv.testmediasofttask.warehouse.service.mappers

import ru.vassuv.testmediasofttask.warehouse.model.dbo.ProductDbo
import ru.vassuv.testmediasofttask.warehouse.model.domain.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.model.domain.DomainProduct
import ru.vassuv.testmediasofttask.warehouse.model.domain.UpdatedProduct
import java.time.LocalDateTime

// TODO возможно вынести все мапперы в отдельный object

internal fun ProductDbo.toDomain(): DomainProduct = DomainProduct(
    // TODO будет ошибка если id==null, но если берем из БД, то всегда существует
    id = requireNotNull(this.id) { "Id не должен быть null у существующего товара." },
    name = this.name,
    article = this.article,
    description = this.description,
    category = this.category,
    price = this.price,
    quantity = this.quantity,
    quantityUpdatedAt = this.quantityUpdatedAt,
    createdAt = this.createdAt
)

internal fun CreatedProduct.toDbo(
    createdAt: LocalDateTime
): ProductDbo = ProductDbo(
    name = name,
    article = article,
    description = description,
    category = category,
    price = price,
    quantity = quantity,
    quantityUpdatedAt = createdAt,
    createdAt = createdAt
)

internal fun ProductDbo.applyWith(
    updatedProduct: UpdatedProduct,
    quantityUpdatedAt: LocalDateTime
): ProductDbo = apply {
    name = updatedProduct.name
    article = updatedProduct.article
    description = updatedProduct.description
    category = updatedProduct.category
    price = updatedProduct.price
    quantity = updatedProduct.quantity
    this@apply.quantityUpdatedAt = quantityUpdatedAt
}