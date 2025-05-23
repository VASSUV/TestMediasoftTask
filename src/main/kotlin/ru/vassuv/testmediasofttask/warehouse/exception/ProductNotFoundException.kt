package ru.vassuv.testmediasofttask.warehouse.exception

import java.util.UUID

/**
 * Ошибка отсутвия продукта в БД с уникальным id
 */
class ProductNotFoundException(val id: UUID): Throwable()


/**
 * Ошибка существования продукта с уникальным артикулом
 */
class ProductIsExistWithArticleException(): Throwable()