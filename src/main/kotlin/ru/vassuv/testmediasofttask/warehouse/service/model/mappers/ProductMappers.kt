package ru.vassuv.testmediasofttask.warehouse.service.model.mappers

import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.service.model.ProductData

/**
 * Мапперы для преобразования сущностей и доменных моделей продуктов в сервисном слое.
 */

/**
 * Преобразует сущность [ProductEntity] в доменную модель [ProductData].
 *
 * @receiver [ProductEntity] сущность товара из базы данных.
 * @return преобразованная доменная модель [ProductData].
 */
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

/**
 * Преобразует доменную модель [CreatedProduct] в новую сущность [ProductEntity].
 *
 * Используется при создании нового товара.
 *
 * @receiver [CreatedProduct] модель создаваемого товара.
 * @return новая сущность [ProductEntity].
 */
internal fun CreatedProduct.toProductEntity(): ProductEntity = ProductEntity(
    name = name,
    article = article,
    description = description,
    category = category,
    price = price,
    quantity = quantity
)
