package ru.vassuv.testmediasofttask.warehouse.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Параметры для Оптимизированного шедулера
 *
 * @property cron - запись времени выполнения как для cron
 * @property percent - величина изменения цены
 */
@ConfigurationProperties(prefix = "scheduling")
data class SchedulingProperties(
    var optimization: Boolean = false,
    var priceChange: PriceChange? = null
)

/**
 * Наборы параметров для разных шедулеров
 *
 * @property simple - Параметры для Простого шедулера
 * @property optimized - Параметры для Оптимизированного шедулера
 */
class PriceChange(
    val simple: SimplePriceChangeProperties? = null,
    val optimized: OptimizedPriceChangeProperties? = null,
)

/**
 * Параметры для Простого шедулера
 *
 * @property cron - запись времени выполнения как для cron
 * @property percent - величина изменения цены
 */
data class SimplePriceChangeProperties(
    var cron: String = "",
    var percent: Float = 0f
)

/**
 * Параметры для Оптимизированного шедулера
 *
 * @property cron - запись времени выполнения как для cron
 * @property percent - величина изменения цены
 */
data class OptimizedPriceChangeProperties(
    var cron: String = "",
    var percent: Float = 0f,
    var batchSize: Int = Int.MAX_VALUE,
    var exportFilePath: String = "",
)
