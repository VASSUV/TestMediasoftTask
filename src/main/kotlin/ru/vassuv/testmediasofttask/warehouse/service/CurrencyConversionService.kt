package ru.vassuv.testmediasofttask.warehouse.service

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import ru.vassuv.testmediasofttask.warehouse.controller.filter.CurrencySession
import ru.vassuv.testmediasofttask.warehouse.enums.CurrencyType
import ru.vassuv.testmediasofttask.warehouse.interaction.model.ExchangesCurrency
import ru.vassuv.testmediasofttask.warehouse.service.model.ExchangeRateInfo

@Service
class CurrencyConversionService(
    private val currencySession: CurrencySession,
    private val currencyProviderService: CurrencyProviderService,
) {

    fun getExchangeRateInfo(): ExchangeRateInfo {
        val targetCurrency = currencySession.getCurrency()
        val currencies = runBlocking { currencyProviderService.getCurrencies() }

        return ExchangeRateInfo(
            targetCurrency,
            targetCurrency.exchangeValue(currencies)
        )
    }
}

private fun CurrencyType.exchangeValue(currencies: ExchangesCurrency) = when (this) {
    CurrencyType.CNY -> currencies.china
    CurrencyType.RUB -> currencies.russia
    CurrencyType.USD -> currencies.usa
}
