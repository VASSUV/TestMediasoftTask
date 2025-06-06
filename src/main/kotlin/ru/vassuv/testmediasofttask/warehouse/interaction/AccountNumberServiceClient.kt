package ru.vassuv.testmediasofttask.warehouse.interaction

import kotlinx.coroutines.runBlocking
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import ru.vassuv.testmediasofttask.warehouse.config.properties.RestServiceProperties
import ru.vassuv.testmediasofttask.warehouse.service.model.AccountNumbers
import ru.vassuv.testmediasofttask.warehouse.service.model.Inns
import ru.vassuv.testmediasofttask.warehouse.service.model.Logins

/**
 * Интерфейс клиента для получения информации о номерах счетов заказчиков
 */
interface AccountNumberServiceClient {

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
class AccountNumberServiceClientImpl(
    webClientBuilder: WebClient.Builder,
    private val restServiceProperties: RestServiceProperties
) : AccountNumberServiceClient {

    private val client = webClientBuilder.baseUrl(restServiceProperties.accountNumber.host).build()

    /**
     * Асинхронно получает информацию о номерах счетов заказчиков
     *
     * @param logins [Logins] список логинов
     * @return [Inns] Список номеров счетов с их логинами
     */
    override fun getByLogins(logins: Logins): AccountNumbers {
        return runBlocking {
            client.post()
                .uri(restServiceProperties.accountNumber.methods.accountNumbers)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(logins)
                .retrieve()
                .awaitBodyOrNull() ?: mapOf()
        }
    }
}
