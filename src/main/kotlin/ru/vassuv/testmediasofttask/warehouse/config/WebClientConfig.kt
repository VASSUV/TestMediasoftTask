package ru.vassuv.testmediasofttask.warehouse.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

/**
 * Конфигурация [WebClient] для взаимодействия с внешними REST-сервисами.
 *
 * В частности, используется для получения информации о курсах валют.
 *
 * @property host базовый URL внешнего REST-сервиса валют, задаётся в properties.
 */
@Configuration
class WebClientConfig(
    @Value("\${rest.currency.host}") private val host: String,
) {
    /**
     * Создаёт настроенный экземпляр [WebClient] для взаимодействия с сервисом валют.
     */
    @Bean
    fun currencyWebClient(builder: WebClient.Builder): WebClient {
        return builder
            .baseUrl(host)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }
}