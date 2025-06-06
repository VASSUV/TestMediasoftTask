package ru.vassuv.testmediasofttask.warehouse.service

import org.springframework.stereotype.Service
import ru.vassuv.testmediasofttask.warehouse.interaction.AccountNumberServiceClient
import ru.vassuv.testmediasofttask.warehouse.interaction.InnServiceClient
import ru.vassuv.testmediasofttask.warehouse.service.model.AccountNumbers
import ru.vassuv.testmediasofttask.warehouse.service.model.Inns
import ru.vassuv.testmediasofttask.warehouse.service.model.Logins

/**
 * Интерфейс сервиса, предоставляющего актуальные дополнительные данные по заказчикам.
 *
 * Выполняет запросы к внешним источникам и обеспечивает кэширование данных.
 */
@Service
class CustomerDataServiceImpl(
    private val innService: InnServiceClient,
    private val accountService: AccountNumberServiceClient
) : CustomerDataService {

    /**
     * Получает набор инн по заданным логинам
     *
     * @param logins логины
     * @return нобор инн по логинам
     */
    override fun getInns(logins: Logins): Inns {
        if (logins.isEmpty()) return emptyMap()
        return innService.getByLogins(logins)
    }

    /**
     * Получает набор номеров счетов по заданным логинам
     *
     * @param logins логины
     * @return нобор номеров счетов по логинам
     */
    override fun getAccountNumbers(logins: Logins): AccountNumbers {
        if (logins.isEmpty()) return emptyMap()
        return accountService.getByLogins(logins)
    }
}
