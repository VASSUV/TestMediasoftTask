package ru.vassuv.testmediasofttask.warehouse.interaction

import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.service.model.Inns
import ru.vassuv.testmediasofttask.warehouse.service.model.Logins

/**
 * Интерфейс клиента для получения информации об инн заказчиков
 */
interface InnServiceClient {

    /**
     * Асинхронно получает информацию об инн заказчиков
     *
     * @param logins [Logins] список логинов
     * @return [Inns] Список инн с их логинами
     */
    fun getByLogins(logins: Logins): Inns
}

/**
 * Интерфейс клиента для получения информации об инн заказчиков
 */
@Component
class StubInnServiceClient : InnServiceClient {

    /**
     * Асинхронно получает информацию об инн заказчиков
     *
     * @param logins [Logins] список логинов
     * @return [Inns] Список инн с их логинами
     */
    @Suppress("MagicNumber")
    override fun getByLogins(logins: Logins):Inns =
        logins.associateWith { "inn-" + it.hashCode().toString().takeLast(12).padStart(12, '0') }
}
