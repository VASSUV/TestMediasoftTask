package ru.vassuv.testmediasofttask.warehouse.service

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import ru.vassuv.testmediasofttask.warehouse.controller.filter.CurrencySession
import ru.vassuv.testmediasofttask.warehouse.enums.CurrencyType
import ru.vassuv.testmediasofttask.warehouse.interaction.model.ExchangesCurrency
import ru.vassuv.testmediasofttask.warehouse.service.model.ExchangeRateInfo

/**
 * Реализация сервиса конвертации валют ([CurrencyConversionService]).
 *
 * Содержит логику получения текущих курсов валют и информации о валюте текущей сессии.
 */
@Service
class CurrencyConversionServiceImpl(
    private val currencySession: CurrencySession,
    private val currencyProviderService: CurrencyProviderService
) : CurrencyConversionService {

    /**
     * Возвращает информацию о текущей валюте сессии и её обменном курсе.
     *
     * Асинхронно запрашивает актуальные курсы валют.
     *
     * @return информация о текущей валюте и её курсе ([ExchangeRateInfo]).
     */
    override fun getExchangeRateInfo(): ExchangeRateInfo {
        val targetCurrency = currencySession.getCurrency()
        val currencies = runBlocking { currencyProviderService.getCurrencies() }

        return ExchangeRateInfo(
            targetCurrency,
            targetCurrency.exchangeValue(currencies)
        )
    }

    /**
     * Возвращает курс обмена для указанного типа валюты.
     *
     * @receiver тип валюты ([CurrencyType]).
     * @param currencies объект с курсами валют ([ExchangesCurrency]).
     * @return текущий обменный курс для типа валюты.
     */
    private fun CurrencyType.exchangeValue(currencies: ExchangesCurrency) = when (this) {
        CurrencyType.CNY -> currencies.china
        CurrencyType.RUB -> currencies.russia
        CurrencyType.USD -> currencies.usa
    }
}