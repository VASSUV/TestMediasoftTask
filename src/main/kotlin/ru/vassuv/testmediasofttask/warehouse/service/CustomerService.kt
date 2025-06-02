package ru.vassuv.testmediasofttask.warehouse.service

import ru.vassuv.testmediasofttask.warehouse.exception.CustomerAlreadyExistsException
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedCustomer
import java.util.*

/**
 * Интерфейс сервиса управления заказчиками.
 *
 * Отвечает за бизнес-логику создания и управления заказчиками.
 */
interface CustomerService {

    /**
     * Создаёт нового заказчика с проверкой уникальности логина и email.
     *
     * @param request данные создаваемого заказчика ([CreatedCustomer]).
     * @return идентификатор созданного заказчика.
     *
     * @throws CustomerAlreadyExistsException если заказчик с указанным логином или email уже существует.
     */
    fun createCustomer(request: CreatedCustomer): UUID
}
