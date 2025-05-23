package ru.vassuv.testmediasofttask.warehouse.service.mappers

import ru.vassuv.testmediasofttask.warehouse.model.dbo.ProductDbo
import ru.vassuv.testmediasofttask.warehouse.model.domain.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.model.domain.DomainProduct
import ru.vassuv.testmediasofttask.warehouse.model.domain.UpdatedProduct
import ru.vassuv.testmediasofttask.warehouse.model.dto.UpdateProductRequestDto
import java.time.LocalDateTime

internal fun ProductDbo.toDomain(): DomainProduct = DomainProduct(
    id = this.id!!,
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