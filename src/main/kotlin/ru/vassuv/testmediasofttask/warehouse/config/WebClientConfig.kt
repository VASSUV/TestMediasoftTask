package ru.vassuv.testmediasofttask.warehouse.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
    @Value("\${rest.currency.host}") private val host: String, // TODO через rest props
) {
    @Bean
    fun currencyWebClient(builder: WebClient.Builder): WebClient {
        return builder
            .baseUrl(host)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }
}