package ru.vassuv.testmediasofttask.warehouse.interaction

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import ru.vassuv.testmediasofttask.warehouse.config.properties.RestServiceProperties
import ru.vassuv.testmediasofttask.warehouse.interaction.rest.model.ExchangesCurrency

/**
 * Интерфейс клиента для получения информации о курсах валют.
 */
interface CurrencyClient {
    /**
     * Асинхронно получает текущие курсы валют.
     *
     * @return [ExchangesCurrency] или null в случае ошибки получения.
     */
    suspend fun fetchCurrencies(): ExchangesCurrency?
}

/**
 * Реализация клиента для взаимодействия с реальным внешним сервисом курсов валют.
 *
 * @property webClientBuilder билдер для веб клиента
 * @property restServiceProperties параметры для rest сервисов
 */
@Service
@ConditionalOnProperty(name = ["rest.currency.mock-enabled"], havingValue = "false", matchIfMissing = true)
class CurrencyClientImpl(
    webClientBuilder: WebClient.Builder,
    private val restServiceProperties: RestServiceProperties
) : CurrencyClient {

    private val log = LoggerFactory.getLogger(this.javaClass)

    private val client = webClientBuilder.baseUrl(restServiceProperties.currency.host).build()

    @Suppress("TooGenericExceptionCaught")
    @Cacheable("currencies", unless = "#result == null")
    override suspend fun fetchCurrencies(): ExchangesCurrency? = try {
        client.get()
            .uri(restServiceProperties.currency.methods.currencies)
            .retrieve()
            .awaitBody<ExchangesCurrency>()
            // .timeout(Duration.ofSeconds(5)) // TODO явный таймаут 5 секунд
            .also { log.info("Currencies are fetched") }
    } catch (ex: Exception) {
        log.error("Could not fetch currencies, ${ex.message}")
        null
    }
}

/**
 * Мок-реализация клиента, возвращающая предопределённые курсы валют для тестирования.
 */
@Service
@ConditionalOnProperty(name = ["rest.currency.mock-enabled"], havingValue = "true")
class CurrencyClientMock : CurrencyClient {
    override suspend fun fetchCurrencies() = ExchangesCurrency(
        china = 0.9.toBigDecimal(),
        usa = 0.8.toBigDecimal(),
        russia = 1.toBigDecimal(),
    )
}
