package ru.vassuv.testmediasofttask.warehouse.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import ru.vassuv.testmediasofttask.warehouse.interaction.CurrencyClient
import ru.vassuv.testmediasofttask.warehouse.interaction.model.ExchangesCurrency

@Service
class CurrencyProviderService(
    @Qualifier("currencyClientImpl") private val currencyClient: CurrencyClient,
    private val objectMapper: ObjectMapper
) {

    @Cacheable("currencies", unless = "#result == null")
    suspend fun getCurrencies(): ExchangesCurrency =
        retry(2) { currencyClient.fetchCurrencies() } ?: fallbackCurrencies

    private val fallbackCurrencies: ExchangesCurrency by lazy {
        javaClass.getResourceAsStream("/static/currencies.json").use {
            objectMapper.readValue(it, ExchangesCurrency::class.java)
        }
    }

    private suspend fun <T> retry(count: Int, block: suspend () -> T): T? {
        for (i in count downTo 1) {
            val result = block()
            if (result != null) {
                return result
            }
        }
        return null
    }
}