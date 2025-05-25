package ru.vassuv.testmediasofttask.warehouse.scheduling

import org.springframework.scheduling.annotation.Scheduled
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import ru.vassuv.testmediasofttask.warehouse.utils.LogExecutionTime
import java.math.BigDecimal

/**
 * Шедулер изменения цены (простой)
 *
 * @property productService Сервис для получения продуктов
 */
open class SimplePriceScheduler(
    private val productService: ProductService
) {

    @Scheduled(fixedRate = 60000) // каждую минуту
    @LogExecutionTime
    open fun updatePrices() {
        val products = productService.findAll()
            .map { it.apply { price += price * BigDecimal("0.01") } } // увеличение цены на 1%

        productService.saveAll(products)
    }
}