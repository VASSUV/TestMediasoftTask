package ru.vassuv.testmediasofttask.warehouse.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import ru.vassuv.testmediasofttask.warehouse.interaction.CurrencyClient
import ru.vassuv.testmediasofttask.warehouse.interaction.model.ExchangesCurrency

/**
 * Реализация сервиса получения актуальных курсов валют ([CurrencyProviderService]).
 *
 * Содержит логику взаимодействия с внешними API и кэширование результатов.
 */
@Service
class CurrencyProviderServiceImpl(
    @Qualifier("currencyClientImpl") private val currencyClient: CurrencyClient,
    private val objectMapper: ObjectMapper
) : CurrencyProviderService {

    /**
     * Асинхронно получает текущие курсы валют с использованием механизма повторных попыток.
     *
     * В случае ошибки использует резервные данные ([fallbackCurrencies]).
     *
     * @return объект с текущими курсами валют ([ExchangesCurrency]).
     */
    override suspend fun getCurrencies(): ExchangesCurrency =
        retry(2) { currencyClient.fetchCurrencies() } ?: fallbackCurrencies

    /**
     * Резервные курсы валют, используемые в случае невозможности получения актуальных данных.
     *
     * Загружаются из локального ресурса (/static/currencies.json).
     */
    private val fallbackCurrencies: ExchangesCurrency by lazy {
        javaClass.getResourceAsStream("/static/currencies.json").use {
            objectMapper.readValue(it, ExchangesCurrency::class.java)
        }
    }

    /**
     * Выполняет повторные попытки асинхронного вызова переданного блока.
     *
     * @param count количество попыток.
     * @param block выполняемый асинхронный блок.
     * @return результат выполнения блока или null, если попытки исчерпаны.
     * TODO сделать retry с логирование
     */
    private suspend fun <T> retry(count: Int, block: suspend () -> T?): T? {
        repeat(count) {
            val result = block()
            if (result != null) {
                return result
            }
        }
        return null
    }
}
