package ru.vassuv.testmediasofttask.warehouse.service

import ru.vassuv.testmediasofttask.warehouse.interaction.rest.model.ExchangesCurrency

/**
 * Интерфейс сервиса, предоставляющего актуальные курсы валют.
 *
 * Выполняет запросы к внешним источникам и обеспечивает кэширование данных.
 */
interface CurrencyProviderService {

    /**
     * Асинхронно получает текущие курсы валют.
     *
     * @return объект с курсами валют ([ExchangesCurrency]).
     */
    suspend fun getCurrencies(): ExchangesCurrency
}
