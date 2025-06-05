package ru.vassuv.testmediasofttask.warehouse.interaction

import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.service.model.AccountNumbers
import ru.vassuv.testmediasofttask.warehouse.service.model.Inns
import ru.vassuv.testmediasofttask.warehouse.service.model.Logins

/**
 * Интерфейс клиента для получения информации о номерах счетов заказчиков
 */
interface AccountServiceClient {

    /**
     * Асинхронно получает информацию о номерах счетов заказчиков
     *
     * @param logins [Logins] список логинов
     * @return [Inns] Список номеров счетов с их логинами
     */
    fun getByLogins(logins: Logins): AccountNumbers
}

/**
 * Интерфейс клиента для получения информации о номерах счетов заказчиков
 */
@Component
class StubAccountServiceClient : AccountServiceClient {

    /**
     * Асинхронно получает информацию о номерах счетов заказчиков
     *
     * @param logins [Logins] список логинов
     * @return [Inns] Список номеров счетов с их логинами
     */
    @Suppress("MagicNumber")
    override fun getByLogins(logins: Logins): AccountNumbers =
        logins.associateWith { "acc-" + it.hashCode().toString().takeLast(9).padStart(9, '0') }
}
