package ru.vassuv.testmediasofttask.warehouse.interaction.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

/**
 * Модель данных, представляющая курсы валют, получаемые от внешнего сервиса.
 *
 * @property china Курс китайского юаня (CNY).
 * @property usa Курс доллара США (USD).
 * @property russia Курс российского рубля (RUB).
 */
data class ExchangesCurrency(
    @JsonProperty(value = "CNY")
    val china: BigDecimal,

    @JsonProperty(value = "USD")
    val usa: BigDecimal,

    @JsonProperty(value = "RUB")
    val russia: BigDecimal,
)
