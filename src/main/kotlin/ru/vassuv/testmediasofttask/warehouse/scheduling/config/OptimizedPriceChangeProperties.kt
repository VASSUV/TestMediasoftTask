package ru.vassuv.testmediasofttask.warehouse.scheduling.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Параметры для Оптимизированного шедулера
 *
 * @property cron - запись времени выполнения как для cron
 * @property percent - величина изменения цены
 */
@ConfigurationProperties(prefix = "scheduling.price-change.optimized")
data class OptimizedPriceChangeProperties(
    var cron: String = "",
    var percent: Float = 0f
)