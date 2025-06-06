package ru.vassuv.testmediasofttask.warehouse.scheduling

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.scheduling.annotation.Scheduled
import ru.vassuv.testmediasofttask.warehouse.aop.LogExecutionTime
import ru.vassuv.testmediasofttask.warehouse.config.properties.SchedulingProperties
import java.io.BufferedWriter
import java.io.FileWriter
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.UUID

/**
 * Оптимизированный шедулер для пакетного изменения цены продуктов и записи результатов в файл.
 *
 * Использует JDBC для эффективного пакетного обновления и логирования продуктов.
 *
 * @property schedulingProperties Конфигурационные свойства для работы шедулера.
 * @property datasourceProperties Конфигурационные свойства JDBC-подключения.
 *
 * @see [SchedulingProperties]
 * @see [DataSourceProperties]
 */
open class OptimizedPriceScheduler(
    private val schedulingProperties: SchedulingProperties,
    private val datasourceProperties: DataSourceProperties,
) {

    private val log = LoggerFactory.getLogger(OptimizedPriceScheduler::class.java)

    /**
     * Запускает задачу по обновлению цен продуктов и записи результатов в файл.
     *
     * Выполняется по расписанию, указанному в конфигурации (`cron` выражение).
     *
     * @see [Scheduled]
     * @see [LogExecutionTime]
     */
    @Scheduled(cron = "\${scheduling.price-change.optimized.cron}")
    @LogExecutionTime
    open fun optimizedPriceUpdate() {
        val optimized = schedulingProperties.priceChange.optimized
        val exportFilePath = optimized.exportFilePath.ifEmpty { error("Export file path parameter is empty") }
        val percent = optimized.percent.takeIf { it != 0f } ?: error("Percent parameter equals zero")
        val batchSize = optimized.batchSize.takeIf { it > 0 } ?: error("Batch size parameter must be more than zero")
        val jdbcUrl: String = datasourceProperties.url
        val username: String = datasourceProperties.username
        val password: String = datasourceProperties.password

        val bufferedWriter = BufferedWriter(FileWriter(exportFilePath, true))
        val connection = DriverManager.getConnection(jdbcUrl, username, password)

        bufferedWriter.use { logFile ->
            connection.use { conn ->
                conn.autoCommit = false
                val sql = "SELECT id, name, description, article, price, created_at FROM products FOR UPDATE"
                conn.prepareStatement(sql).use { selectStatement ->
                    selectStatement.executeQuery().use { resultSet ->
                        updatePricesAndWriteToFile(conn, batchSize, resultSet, percent, logFile, exportFilePath)
                    }
                }
            }
        }
    }

    /**
     * Пакетное обновление цен и запись информации о продуктах в файл.
     *
     * @param connection JDBC-соединение с базой данных.
     * @param batchSize Размер пакета для выполнения обновлений.
     * @param resultSet Результат запроса продуктов.
     * @param percent Процент изменения цены.
     * @param logFile Буферизированный поток записи в файл.
     * @param exportFilePath Путь к файлу экспорта данных.
     */
    @Suppress("TooGenericExceptionCaught")
    private fun updatePricesAndWriteToFile(
        connection: Connection,
        batchSize: Int,
        resultSet: ResultSet,
        percent: Float,
        logFile: BufferedWriter,
        exportFilePath: String
    ) {
        connection.prepareStatement("UPDATE products SET price = ? WHERE id = ?").use { updateStatement ->
            try {
                var batchCounter = batchSize
                // TODO возможно следуюет выбирать batch size динамически, из размера таблицы
                var batchNumber = 0

                while (resultSet.next()) {
                    val id = UUID.fromString(resultSet.getString("id"))
                    val name = resultSet.getString("name")
                    val description = resultSet.getString("description")
                    val article = resultSet.getString("article")
                    val createdAt = resultSet.getString("created_at")
                    val oldPrice = resultSet.getBigDecimal("price")
                    val newPrice = oldPrice + oldPrice * percent.toBigDecimal()

                    logFile.write("$id $name $newPrice $description $article $createdAt\n")

                    updateStatement.setBigDecimal(1, newPrice)
                    updateStatement.setObject(2, id)
                    updateStatement.addBatch()

                    batchCounter--
                    if (batchCounter == 0) {
                        batchCounter = batchSize
                        updateStatement.executeBatch()
                        // TODO можно обработать результат, и понять запись в которой была ошибка
                        log.info("Успешно записан батч ($batchNumber) из $batchSize продуктов в файл $exportFilePath")
                        batchNumber++
                    }
                }

                updateStatement.executeBatch() // TODO можно обработать результат, и понять запись в которой была ошибка
                val currentBatchSize = batchSize - batchCounter
                if (currentBatchSize != 0) {
                    log.info(
                        "Успешно записан батч ($batchNumber) из $currentBatchSize продуктов в файл $exportFilePath"
                    )
                }
                connection.commit()
                logFile.flush()
            } catch (e: Exception) {
                connection.rollback()
                log.error("Ошибка при обновлении цен продуктов: ${e.message}", e)
            }
        }
    }
}
