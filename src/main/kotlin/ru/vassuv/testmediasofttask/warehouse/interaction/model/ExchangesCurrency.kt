package ru.vassuv.testmediasofttask.warehouse.interaction.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class ExchangesCurrency(
    @JsonProperty(value = "CNY")
    val china: BigDecimal,
    @JsonProperty(value = "USD")
    val usa: BigDecimal,
    @JsonProperty(value = "RUB")
    val russia: BigDecimal,
)