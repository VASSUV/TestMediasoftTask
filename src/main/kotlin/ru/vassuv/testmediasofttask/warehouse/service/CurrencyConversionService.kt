package ru.vassuv.testmediasofttask.warehouse.service

import ru.vassuv.testmediasofttask.warehouse.service.model.ExchangeRateInfo

/**
 * Интерфейс сервиса конвертации валют.
 *
 * Отвечает за получение текущих курсов валют и информации о валюте текущей сессии.
 */
interface CurrencyConversionService {

    /**
     * Возвращает информацию о текущем курсе валюты, установленной в сессии.
     *
     * @return объект с информацией о текущей валюте и её курсе ([ExchangeRateInfo]).
     */
    fun getExchangeRateInfo(): ExchangeRateInfo
}
