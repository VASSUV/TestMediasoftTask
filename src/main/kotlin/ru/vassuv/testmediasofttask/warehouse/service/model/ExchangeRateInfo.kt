package ru.vassuv.testmediasofttask.warehouse.service.model

import ru.vassuv.testmediasofttask.warehouse.enums.CurrencyType
import java.math.BigDecimal

data class ExchangeRateInfo(
    val currencyType: CurrencyType,
    val convertValue: BigDecimal,
)