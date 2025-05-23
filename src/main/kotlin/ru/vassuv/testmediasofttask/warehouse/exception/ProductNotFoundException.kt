package ru.vassuv.testmediasofttask.warehouse.exception

import java.util.UUID

/**
 * Ошибка отсутвия продукта в БД с уникальным id
 */
class ProductNotFoundException(val id: UUID): RuntimeException("Товар с id=$id не найден")


/**
 * Ошибка существования продукта с уникальным артикулом
 */
class ProductIsExistWithArticleException(): RuntimeException("Товар с таким артикулом уже существует")