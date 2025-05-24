package ru.vassuv.testmediasofttask.warehouse.scheduling

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import ru.vassuv.testmediasofttask.warehouse.utils.LogExecutionTime
import java.math.BigDecimal

open class SimplePriceScheduler(
    private val productService: ProductService
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(fixedRate = 60000) // каждую минуту
    @LogExecutionTime
    open fun updatePrices() {
        log.info("Start updatePrices() in SimplePriceScheduler ")
        val products = productService.findAll()
            .map { it.apply { price += price * BigDecimal("0.01") } } // увеличение цены на 1%

        productService.saveAll(products)
        log.info("Finish updatePrices() in SimplePriceScheduler ")
    }
}