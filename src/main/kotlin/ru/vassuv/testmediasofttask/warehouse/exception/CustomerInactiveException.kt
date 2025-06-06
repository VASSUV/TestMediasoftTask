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
@ResponseStatus(HttpStatus.FORBIDDEN)
class CustomerInactiveException(
    val customerId: UUID
) : RuntimeException("Пользователь с id=$customerId неактивен")
