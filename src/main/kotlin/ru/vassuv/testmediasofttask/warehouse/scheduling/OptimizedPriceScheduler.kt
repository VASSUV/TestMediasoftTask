package ru.vassuv.testmediasofttask.warehouse.scheduling

import org.springframework.scheduling.annotation.Scheduled
import ru.vassuv.testmediasofttask.warehouse.config.properties.SchedulingProperties
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
    private val properties: SchedulingProperties,
    private val productService: ProductService,
    private val productExportService: ProductExportService
) {

    @Suppress("ReturnCount")
    @Scheduled(cron = "\${scheduling.price-change.optimized.cron}")
    @LogExecutionTime
    open fun optimizedPriceUpdate() {
        val optimized = properties.priceChange?.optimized ?: return
        val exportFilePath = optimized.exportFilePath.ifEmpty { return }
        val percent = optimized.percent.takeIf { it != 0f } ?: return
        val batchSize = optimized.batchSize.takeIf { it > 0 } ?: return
        val filePath = Paths.get(exportFilePath)
        productService.updatePricesInBatches(percent.toBigDecimal(), batchSize) { batch ->
            productExportService.exportProductsBatchToFile(batch, filePath)
        }
    }
}
