package ru.vassuv.testmediasofttask.warehouse.interaction

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import ru.vassuv.testmediasofttask.warehouse.interaction.model.ExchangesCurrency

interface CurrencyClient {
    suspend fun fetchCurrencies(): ExchangesCurrency?
}

@Service
@ConditionalOnProperty(name = ["rest.currency.mock-enabled"], havingValue = "false", matchIfMissing = true)
class CurrencyClientImpl(
    private val webClient: WebClient,
    @Value("\${rest.currency.methods.currencies}") private val method: String
) : CurrencyClient {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override suspend fun fetchCurrencies(): ExchangesCurrency? = try {
        webClient.get()
            .uri(method)
            .retrieve() // retry web client
            .awaitBody<ExchangesCurrency>()
            .also { log.info("Currencies are fetched") }
    } catch (ex: Exception) {
        log.error("Could not fetch currencies, ${ex.message}")
        null
    }
}

@Service
@ConditionalOnProperty(name = ["rest.currency.mock-enabled"], havingValue = "true")
class CurrencyClientMock(
) : CurrencyClient {
    override suspend fun fetchCurrencies() = ExchangesCurrency(
        china = 0.9.toBigDecimal(),
        usa = 0.8.toBigDecimal(),
        russia = 1.toBigDecimal(),
    )
}