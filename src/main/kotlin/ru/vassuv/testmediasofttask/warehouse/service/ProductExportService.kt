package ru.vassuv.testmediasofttask.warehouse.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.model.dbo.ProductDbo
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

/**
 * Сервис сохранения продуктов из БД в файл
 */
@Component
class ProductExportService {


    private val log = LoggerFactory.getLogger(ProductExportService::class.java)

    fun exportProductsBatchToFile(products: List<ProductDbo>, filePath: Path) {
        Files.createDirectories(filePath.parent)
        Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND).use { writer ->
            products.forEach { product ->
                writer.write("${product.id},${product.name},${product.price}\n")
            }
            log.info("Успешно записал батч из ${products.size} продуктов в файл $filePath")
        }
    }
}