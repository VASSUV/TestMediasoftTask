package ru.vassuv.testmediasofttask.warehouse.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

/**
 * Ошибка отсутвия продукта в БД с уникальным id
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class ProductNotFoundException(val id: UUID): RuntimeException("Товар с id=$id не найден")
