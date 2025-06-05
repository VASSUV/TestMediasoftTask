package ru.vassuv.testmediasofttask.warehouse.service

import ru.vassuv.testmediasofttask.warehouse.service.model.AccountNumbers
import ru.vassuv.testmediasofttask.warehouse.service.model.Inns
import ru.vassuv.testmediasofttask.warehouse.service.model.Logins

/**
 * Интерфейс сервиса, предоставляющего актуальные дополнительные данные по заказчикам.
 *
 * Выполняет запросы к внешним источникам и обеспечивает кэширование данных.
 */
interface CustomerDataService {

    /**
     * Получает набор инн по заданным логинам
     *
     * @param logins логины
     * @return нобор инн по логинам
     */
    fun getInns(logins: Logins): Inns

    /**
     * Получает набор номеров счетов по заданным логинам
     *
     * @param logins логины
     * @return нобор номеров счетов по логинам
     */
    fun getAccountNumbers(logins: Logins): AccountNumbers
}
