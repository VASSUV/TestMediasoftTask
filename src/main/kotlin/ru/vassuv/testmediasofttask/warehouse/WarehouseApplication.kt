package ru.vassuv.testmediasofttask.warehouse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

/**
 * Основной класс запуска Spring Boot приложения "Warehouse".
 *
 * Включает сканирование конфигурационных свойств и поддержку кэширования.
 *
 * @see [EnableCaching] - поддержка кэширования.
 * @see [ConfigurationPropertiesScan] - автоматическое сканирование конфигурационных свойств.
 */
@ConfigurationPropertiesScan
@SpringBootApplication
@EnableCaching
class WarehouseApplication

/**
 * Основная точка входа в приложение.
 *
 * @param args аргументы командной строки.
 */
fun main(args: Array<String>) {
	runApplication<WarehouseApplication>(args = args)
}
