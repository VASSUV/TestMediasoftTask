package ru.vassuv.testmediasofttask.warehouse.scheduling

import org.springframework.scheduling.annotation.Scheduled
import ru.vassuv.testmediasofttask.warehouse.scheduling.config.OptimizedPriceChangeProperties
import ru.vassuv.testmediasofttask.warehouse.service.ProductExportService
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import ru.vassuv.testmediasofttask.warehouse.utils.LogExecutionTime
import java.nio.file.Paths

/**
 * Шедулер изменения цены (оптимизированный, сложный)
 *
 * @property properties Параметры для оптимизированного шедулера
 * @property productService Сервис для получения продуктов
 */
open class OptimizedPriceScheduler(
    private val properties: OptimizedPriceChangeProperties,
    private val productService: ProductService,
    private val productExportService: ProductExportService
) {

    @Scheduled(cron = "\${scheduling.price-change.optimized.cron}")
    @LogExecutionTime
    open fun optimizedPriceUpdate() {
        val filePath = Paths.get(properties.exportFilePath)

        productService.updatePricesInBatches(
            percent = properties.percent.toBigDecimal(),
            batchSize = properties.batchSize
        ) { batch ->
            productExportService.exportProductsBatchToFile(batch, filePath)
        }
    }
}