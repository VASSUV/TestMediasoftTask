package ru.vassuv.testmediasofttask.warehouse.service.mappers

import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.service.model.ProductData
import java.time.LocalDateTime

// TODO возможно вынести все мапперы в отдельный object

internal fun ProductEntity.toDomain(): ProductData = ProductData(
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
    createdAt: LocalDateTime // TODO можно использовать @CreationTimestamp или в БД
): ProductEntity = ProductEntity(
    name = name,
    article = article,
    description = description,
    category = category,
    price = price,
    quantity = quantity,
    quantityUpdatedAt = createdAt,
    createdAt = createdAt
)
