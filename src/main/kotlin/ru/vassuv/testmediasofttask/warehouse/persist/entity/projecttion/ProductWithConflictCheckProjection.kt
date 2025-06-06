package ru.vassuv.testmediasofttask.warehouse.persist.entity.projecttion

import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity

/**
 * Проекция для проверки наличия конфликта продукта по артикулу.
 *
 * Используется для быстрого определения существования товара по артикулу,
 * а также для получения самого продукта, если он существует.
 */
interface ProductWithConflictCheckProjection {

    /** Сущность продукта, если найдена в базе данных. */
    val product: ProductEntity?

    /**
     * Флаг, указывающий, существует ли уже продукт с указанным артикулом.
     * true – если продукт с артикулом уже существует.
     */
    val articleExists: Boolean
}