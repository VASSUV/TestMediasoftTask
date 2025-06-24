package ru.vassuv.currencyservice.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import java.math.BigDecimal
import kotlin.random.Random

@Service
class CurrencyService {

    private val currencies = mapOf(
        "USD" to BigDecimal("0.011"),
        "CNY" to BigDecimal("0.025"),
        "RUB" to BigDecimal("1")
    )

    fun getCurrencies(): Map<String, BigDecimal> {
        if (Random.nextBoolean()) { // 50% шанс исключения
            throw CurrencyServiceException("Service temporarily unavailable")
        }
        return currencies
    }
}

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
class CurrencyServiceException(message: String) : RuntimeException(message)