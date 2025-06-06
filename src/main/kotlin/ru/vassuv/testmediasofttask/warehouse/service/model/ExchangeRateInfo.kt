package ru.vassuv.testmediasofttask.warehouse.service.model

import ru.vassuv.testmediasofttask.warehouse.enums.CurrencyType
import java.math.BigDecimal

/**
 * Информация о курсе валюты для конвертации цен.
 *
 * @property currencyType Тип валюты.
 * @property convertValue Коэффициент конвертации валюты.
 */
data class ExchangeRateInfo(
    val currencyType: CurrencyType,
    val convertValue: BigDecimal
)
