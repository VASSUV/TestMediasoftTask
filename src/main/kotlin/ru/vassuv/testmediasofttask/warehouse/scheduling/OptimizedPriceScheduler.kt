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
 * Шедулер изменения цены (оптимизированный, сложный)
 *
 * @property schedulingProperties Параметры для оптимизированного шедулера
 * @property datasourceProperties Параметры для JDBC подключения
 */

open class OptimizedPriceScheduler(
    private val schedulingProperties: SchedulingProperties,
    private val datasourceProperties: DataSourceProperties,
) {

    private val log = LoggerFactory.getLogger(OptimizedPriceScheduler::class.java)

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
            connection.use { connection ->
                connection.autoCommit = false // Включаем транзакцию вручную
                val sql = "SELECT id, name, description, article, price, created_at FROM products FOR UPDATE"
                val prepareStatement = connection
                    .prepareStatement(sql)
                prepareStatement.use { selectStatement ->
                    selectStatement.executeQuery().use { resultSet ->
                        updatePricesAndWriteToFile(connection, batchSize, resultSet, percent, logFile, exportFilePath)
                    }
                }
            }
        }
    }

    private fun updatePricesAndWriteToFile(
        connection: Connection,
        batchSize: Int,
        resultSet: ResultSet,
        percent: Float,
        logFile: BufferedWriter,
        exportFilePath: String
    ) {
        val updateStatement = connection.prepareStatement("UPDATE products SET price = ? WHERE id = ?")

        try {
            var batchDecrease = batchSize
            var batchNumber = 0

            while (resultSet.next()) {
                val id = UUID.fromString(resultSet.getString("id"))
                val name = resultSet.getString("name")
                val description = resultSet.getString("description")
                val article = resultSet.getString("article")
                val createdAt = resultSet.getString("created_at")
                val oldPrice = resultSet.getBigDecimal("price")
                val newPrice = oldPrice + oldPrice * percent.toBigDecimal()

                // Пишем лог
                logFile.write("$id $name $newPrice $description $article $createdAt\n")

                // Обновляем цену
                updateStatement.setBigDecimal(1, newPrice)
                updateStatement.setObject(2, id)
                updateStatement.addBatch()
                batchDecrease--
                if (batchDecrease == 0) {
                    batchDecrease = batchSize
                    updateStatement.executeBatch()
                    log.info("Успешно записал батч ($batchNumber) из $batchSize продуктов в файл $exportFilePath")
                    batchNumber++
                }
            }

            updateStatement.executeBatch()
            val currentButchSize = batchSize - batchDecrease
            if (currentButchSize != 0) {
                log.info("Успешно записал батч ($batchNumber) из $currentButchSize продуктов в файл $exportFilePath")
            }
            connection.commit()
            logFile.flush()
        } catch (e: Exception) {
            connection.rollback()
            log.error(e.message, e)
        } finally {
            updateStatement.close()
        }
    }
}
