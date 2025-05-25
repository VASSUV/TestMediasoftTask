package ru.vassuv.testmediasofttask.warehouse.scheduling

import org.springframework.scheduling.annotation.Scheduled
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import ru.vassuv.testmediasofttask.warehouse.utils.LogExecutionTime
import java.math.BigDecimal

/**
 * Шедулер изменения цены (оптимизированный, сложный)
 *
 * @property productService Сервис для получения продуктов
 */
open class OptimizedPriceScheduler(
    private val productService: ProductService
) {

    @Scheduled(fixedRate = 60000)
    @LogExecutionTime
    open fun optimizedPriceUpdate() {
        productService.updateAllPrices(BigDecimal("-0.01")) // уменьшение на 1%
    }
}