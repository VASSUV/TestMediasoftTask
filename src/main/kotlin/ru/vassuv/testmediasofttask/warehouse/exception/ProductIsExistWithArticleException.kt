package ru.vassuv.testmediasofttask.warehouse.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Ошибка существования продукта с уникальным артикулом
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
class ProductIsExistWithArticleException: RuntimeException("Товар с таким артикулом уже существует")
