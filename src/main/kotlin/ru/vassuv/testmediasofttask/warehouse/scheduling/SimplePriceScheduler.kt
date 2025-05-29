package ru.vassuv.testmediasofttask.warehouse.scheduling

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.scheduling.annotation.Scheduled
import ru.vassuv.testmediasofttask.warehouse.config.properties.SchedulingProperties
import ru.vassuv.testmediasofttask.warehouse.service.ProductServiceImpl
import ru.vassuv.testmediasofttask.warehouse.aop.LogExecutionTime
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import java.io.BufferedWriter
import java.io.FileWriter
import java.math.BigDecimal
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.UUID
import kotlin.text.ifEmpty
import kotlin.use

/**
 * Шедулер изменения цены (простой)
 *
 * @property schedulingProperties Параметры для простого шедулера
 * @property datasourceProperties Параметры подключения к БД
 */
open class SimplePriceScheduler(
    private val schedulingProperties: SchedulingProperties,
    private val datasourceProperties: DataSourceProperties,
) {

    private val log = LoggerFactory.getLogger(SimplePriceScheduler::class.java)

    @Scheduled(cron = "\${scheduling.price-change.simple.cron}") // каждую минуту
    @LogExecutionTime
    open fun updatePrices() {
        val simple = schedulingProperties.priceChange.simple
        val percent = simple.percent.toBigDecimal()
        val exportFilePath = simple.exportFilePath.ifEmpty { error("Export file path parameter is empty") }

        val jdbcUrl: String = datasourceProperties.url
        val username: String = datasourceProperties.username
        val password: String = datasourceProperties.password

        val bufferedWriter = BufferedWriter(FileWriter(exportFilePath, true))
        val connection = DriverManager.getConnection(jdbcUrl, username, password)
        bufferedWriter.use { logFile ->
            connection.use { connection ->
                connection.autoCommit = false // Включаем транзакцию вручную
                val prepareStatement = connection
                    .prepareStatement("UPDATE products SET price = price + price * ? RETURNING *")

                prepareStatement.setBigDecimal(1, percent)
                prepareStatement.use { selectStatement ->
                    selectStatement.executeQuery().use { resultSet ->
                        logToFile(resultSet, logFile, exportFilePath)
                        connection.commit()
                    }
                }
            }
        }
    }

    private fun logToFile(
        resultSet: ResultSet,
        logFile: BufferedWriter,
        exportFilePath: String
    ) {

        var count = 0
        while (resultSet.next()) {
            val id = UUID.fromString(resultSet.getString("id"))
            val name = resultSet.getString("name")
            val description = resultSet.getString("description")
            val article = resultSet.getString("article")
            val createdAt = resultSet.getString("created_at")
            val price = resultSet.getBigDecimal("price")
            logFile.write("$id $name $price $description $article $createdAt\n")
            count++
        }

        log.info("Успешно записал $count продуктов в файл $exportFilePath")
        logFile.flush()
    }
}
