package ru.vassuv.testmediasofttask.warehouse.scheduling

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import ru.vassuv.testmediasofttask.warehouse.utils.LogExecutionTime
import java.math.BigDecimal

open class OptimizedPriceScheduler(
    private val productService: ProductService
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(fixedRate = 60000)
    @LogExecutionTime
    open fun optimizedPriceUpdate() {
        productService.updateAllPrices(BigDecimal("-0.01")) // уменьшение на 1%
    }
}