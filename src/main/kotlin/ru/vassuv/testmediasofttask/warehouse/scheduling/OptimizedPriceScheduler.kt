package ru.vassuv.testmediasofttask.warehouse.scheduling

import org.springframework.scheduling.annotation.Scheduled
import ru.vassuv.testmediasofttask.warehouse.scheduling.config.OptimizedPriceChangeProperties
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import ru.vassuv.testmediasofttask.warehouse.utils.LogExecutionTime

/**
 * Шедулер изменения цены (оптимизированный, сложный)
 *
 * @property properties Параметры для оптимизированного шедулера
 * @property productService Сервис для получения продуктов
 */
open class OptimizedPriceScheduler(
    private val properties: OptimizedPriceChangeProperties,
    private val productService: ProductService
) {

    @Scheduled(cron = "\${scheduling.price-change.optimized.cron}")
    @LogExecutionTime
    open fun optimizedPriceUpdate() {
        productService.updateAllPrices(properties.percent.toBigDecimal()) // уменьшение на 1%
    }
}