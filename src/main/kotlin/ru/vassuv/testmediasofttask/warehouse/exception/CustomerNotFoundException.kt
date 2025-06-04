package ru.vassuv.testmediasofttask.warehouse.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

/**
 * Ошибка, возникающая при попытке выполнить действие с пользователем,
 * который не найден в базе данных или имеет статус неактивного.
 *
 * @property customerId UUID пользователя, вызвавшего ошибку
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class CustomerNotFoundException(
    val customerId: UUID
) : RuntimeException("Пользователь с id=$customerId не найден")

/**
 * Метод вызывающий исключение CustomerNotFoundException
 *
 * @property customerId UUID пользователя, вызвавшего ошибку
 */
fun customerNotFoundError(customerId: UUID): Nothing = throw CustomerNotFoundException(customerId)