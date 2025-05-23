package ru.vassuv.testmediasofttask.warehouse.controller.mappers

import ru.vassuv.testmediasofttask.warehouse.model.domain.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.model.domain.DomainProduct
import ru.vassuv.testmediasofttask.warehouse.model.domain.UpdatedProduct
import ru.vassuv.testmediasofttask.warehouse.model.dto.CreateProductRequestDto
import ru.vassuv.testmediasofttask.warehouse.model.dto.ProductResponseDto
import ru.vassuv.testmediasofttask.warehouse.model.dto.UpdateProductRequestDto

internal fun DomainProduct.toResponseDto(): ProductResponseDto = ProductResponseDto(
    id = this.id,
    name = this.name,
    article = this.article,
    description = this.description,
    category = this.category,
    price = this.price,
    quantity = this.quantity,
    quantityUpdatedAt = this.quantityUpdatedAt,
    createdAt = this.createdAt
)



internal fun CreateProductRequestDto.toDomain(): CreatedProduct = CreatedProduct(
    name = name,
    article = article,
    description = description,
    category = category,
    price = price,
    quantity = quantity
)

internal fun UpdateProductRequestDto.toDomain(): UpdatedProduct = UpdatedProduct(
    name = name,
    article = article,
    description = description,
    category = category,
    price = price,
    quantity = quantity
)