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
 * Простой шедулер для обновления цены продуктов и записи обновлённых данных в файл.
 *
 * Использует JDBC-запросы для обновления всех цен продуктов единым запросом с использованием RETURNING.
 *
 * @property schedulingProperties Конфигурационные свойства шедулера.
 * @property datasourceProperties Параметры подключения к базе данных.
 *
 * @see [SchedulingProperties]
 * @see [DataSourceProperties]
 */
open class SimplePriceScheduler(
    private val schedulingProperties: SchedulingProperties,
    private val datasourceProperties: DataSourceProperties,
) {

    private val log = LoggerFactory.getLogger(SimplePriceScheduler::class.java)

    /**
     * Запускает обновление цен продуктов и запись результатов в файл согласно расписанию.
     *
     * @see [Scheduled]
     * @see [LogExecutionTime]
     */
    @Scheduled(cron = "\${scheduling.price-change.simple.cron}")
    @LogExecutionTime
    open fun updatePrices() {
        val simple = schedulingProperties.priceChange.simple
        val percent = simple.percent.toBigDecimal()
        val exportFilePath = simple.exportFilePath.ifEmpty { error("Export file path parameter is empty") }

        val jdbcUrl = datasourceProperties.url
        val username = datasourceProperties.username
        val password = datasourceProperties.password

        val bufferedWriter = BufferedWriter(FileWriter(exportFilePath, true))
        val connection = DriverManager.getConnection(jdbcUrl, username, password)

        bufferedWriter.use { logFile ->
            connection.use { conn ->
                conn.autoCommit = false
                val sql = "UPDATE products SET price = price + price * ? RETURNING *"
                conn.prepareStatement(sql).use { statement ->
                    statement.setBigDecimal(1, percent)
                    statement.executeQuery().use { resultSet ->
                        logToFile(resultSet, logFile, exportFilePath)
                        conn.commit()
                    }
                }
            }
        }
    }

    /**
     * Записывает обновлённые данные продуктов в указанный файл.
     *
     * @param resultSet Результат выполнения запроса с обновлёнными данными.
     * @param logFile Поток записи для логирования данных.
     * @param exportFilePath Путь к файлу экспорта данных.
     */
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

        log.info("Успешно записано $count продуктов в файл $exportFilePath")
        logFile.flush()
    }
}
