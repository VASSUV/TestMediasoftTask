package ru.vassuv.testmediasofttask.warehouse.scheduling

import org.springframework.scheduling.annotation.Scheduled
import ru.vassuv.testmediasofttask.warehouse.config.properties.SchedulingProperties
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import ru.vassuv.testmediasofttask.warehouse.utils.LogExecutionTime

/**
 * Шедулер изменения цены (простой)
 *
 * @property properties Параметры для простого шедулера
 * @property productService Сервис для получения продуктов
 */
open class SimplePriceScheduler(
    private val properties: SchedulingProperties,
    private val productService: ProductService
) {

    @Scheduled(cron = "\${scheduling.price-change.simple.cron}") // каждую минуту
    @LogExecutionTime
    open fun updatePrices() {
        val persent = properties.priceChange?.simple?.percent?.toBigDecimal()
            ?: return

        val products = productService.findAll().asSequence()
            .map { it.apply { price += price * persent } } // увеличение цены на persent

        productService.saveAll(products)
    }
}
