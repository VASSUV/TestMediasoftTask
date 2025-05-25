package ru.vassuv.testmediasofttask.warehouse.scheduling

import org.springframework.scheduling.annotation.Scheduled
import ru.vassuv.testmediasofttask.warehouse.scheduling.config.SimplePriceChangeProperties
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import ru.vassuv.testmediasofttask.warehouse.utils.LogExecutionTime
import java.math.BigDecimal

/**
 * Шедулер изменения цены (простой)
 *
 * @property properties Параметры для простого шедулера
 * @property productService Сервис для получения продуктов
 */
open class SimplePriceScheduler(
    private val properties: SimplePriceChangeProperties,
    private val productService: ProductService
) {

    @Scheduled(cron = "\${scheduling.price-change.simple.cron}") // каждую минуту
    @LogExecutionTime
    open fun updatePrices() {
        val products = productService.findAll()
            .map { it.apply { price += price * properties.percent.toBigDecimal() } } // увеличение цены на 1%

        productService.saveAll(products)
    }
}