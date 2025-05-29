package ru.vassuv.testmediasofttask.warehouse.mock

import ru.vassuv.testmediasofttask.warehouse.controller.request.CreateProductRequest
import ru.vassuv.testmediasofttask.warehouse.enums.ProductCategoryType
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID

internal fun createProductRequestMock(
    name: String = "Banana",
    article: String = "banana-article",
    description: String = "Banana description",
    category: ProductCategoryType = ProductCategoryType.FOOD,
    price: BigDecimal = 100.toBigDecimal(),
    quantity: BigDecimal = 10.toBigDecimal()
) = CreateProductRequest(name, article, description, category, quantity, price)

internal fun productEntityMock(
    id: UUID = UUID.randomUUID(),
    name: String = "Banana",
    article: String = "banana-article",
    description: String = "Banana description",
    category: ProductCategoryType = ProductCategoryType.FOOD,
    price: BigDecimal = 100.toBigDecimal(),
    quantity: BigDecimal = 10.toBigDecimal(),
    quantityUpdatedAt: ZonedDateTime = ZonedDateTime.now(),
    createdAt: LocalDate = LocalDate.now()
) = ProductEntity(id, name, article, description, category, price, quantity, quantityUpdatedAt, createdAt)
