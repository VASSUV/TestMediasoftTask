package ru.vassuv.testmediasofttask.warehouse.interaction.rest

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
class InnServiceClientImpl(
    webClientBuilder: WebClient.Builder,
    private val restServiceProperties: RestServiceProperties
) : InnServiceClient {

    private val client = webClientBuilder.baseUrl(restServiceProperties.inn.host).build()

    /**
     * Асинхронно получает информацию об инн заказчиков
     *
     * @param logins [Logins] список логинов
     * @return [Inns] Список инн с их логинами
     */
    override fun getByLogins(logins: Logins): AccountNumbers {
        return runBlocking {
            client.post()
                .uri(restServiceProperties.inn.methods.inns)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(logins)
                .retrieve()
                .awaitBodyOrNull() ?: mapOf()
        }
    }
}
