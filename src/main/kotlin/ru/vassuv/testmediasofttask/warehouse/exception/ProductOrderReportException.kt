package ru.vassuv.testmediasofttask.warehouse.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Ошибка, возникающая при составлении отчетов по продуктам и заказам.
 *
 * @property reason причина недоступности товара
 * @property cause ошибка которая вызвала исключение
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
class ProductOrderReportException(
    reason: String,
    cause: Throwable? = null,
) : RuntimeException("Ошибка при составлении отчета: $reason", cause)

/**
 * Метод вызывающий исключение ProductOrderReportException
 *
 * @throws ProductOrderReportException если товара недостаточно или он недоступен.
 */
fun productOrderReportError(reason: String, cause: Throwable? = null): Nothing =
    throw ProductOrderReportException(reason, cause)
