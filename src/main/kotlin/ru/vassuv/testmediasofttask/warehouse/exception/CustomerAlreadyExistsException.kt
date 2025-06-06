package ru.vassuv.testmediasofttask.warehouse.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Ошибка создания заказчика при попытке использовать уже существующий логин или email.
 *
 * @property login логин заказчика.
 * @property email email заказчика.
 */
@ResponseStatus(HttpStatus.CONFLICT)
class CustomerAlreadyExistsException(
    val login: String,
    val email: String
) : RuntimeException("Заказчик с login='$login' или email='$email' уже существует")
