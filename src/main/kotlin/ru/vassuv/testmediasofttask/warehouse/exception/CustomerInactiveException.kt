package ru.vassuv.testmediasofttask.warehouse.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import ru.vassuv.testmediasofttask.warehouse.persist.entity.CustomerEntity
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

/**
 * Метод вызывающий исключение CustomerInactiveException
 *
 * @property customerId UUID пользователя, вызвавшего ошибку
 * @throws CustomerInactiveException если заказчик не активный
 */
fun customerInactiveError(customerId: UUID): Nothing = throw CustomerInactiveException(customerId)

/**
 * Метод проверяющий активность заказчика
 *
 * @property customerId UUID пользователя, вызвавшего ошибку
 * @throws CustomerInactiveException если заказчик не активный
 */
fun CustomerEntity.checkCustomerInactive() {
    if(!isActive) {
        throw CustomerInactiveException(this.id!!)
    }
}
