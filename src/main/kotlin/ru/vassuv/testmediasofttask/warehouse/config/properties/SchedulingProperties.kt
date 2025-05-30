package ru.vassuv.testmediasofttask.warehouse.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Параметры для Оптимизированного шедулера
 *
 * @property cron - запись времени выполнения как для cron
 * @property percent - величина изменения цены
 */
@ConfigurationProperties(prefix = "scheduling")
data class SchedulingProperties(
    var optimization: Boolean = false,
    var priceChange: PriceChange
)

/**
 * Наборы параметров для разных шедулеров
 *
 * @property simple - Параметры для Простого шедулера
 * @property optimized - Параметры для Оптимизированного шедулера
 * @property entityManager - Параметры для шедулера работающего на EntityManager
 */
class PriceChange(
    val simple: SimplePriceChangeProperties,
    val optimized: OptimizedPriceChangeProperties,
    val entityManager: EntityManagerPriceChangeProperties,
)

/**
 * Параметры для Простого шедулера
 *
 * @property cron - запись времени выполнения как для cron
 * @property percent - величина изменения цены
 * @property batchSize - величина пакета, для отправки изменений в бд порцией
 * @property exportFilePath - путь до файла, в который будут записываться продукты
 */
data class SimplePriceChangeProperties(
    var cron: String,
    var percent: Float,
    var batchSize: Int,
    var exportFilePath: String,
)

/**
 * Параметры для Оптимизированного шедулера
 *
 * @property cron - запись времени выполнения как для cron
 * @property percent - величина изменения цены
 * @property batchSize - величина пакета, для отправки изменений в бд порцией
 * @property exportFilePath - путь до файла, в который будут записываться продукты
 */
data class OptimizedPriceChangeProperties(
    var cron: String,
    var percent: Float,
    var batchSize: Int,
    var exportFilePath: String,
)

/**
 * Параметры для шедулера работающего на EntityManager
 *
 * @property enabled - флаг включающий шедулер
 * @property cron - запись времени выполнения как для cron
 * @property percent - величина изменения цены
 * @property batchSize - величина пакета, для отправки изменений в бд порцией
 * @property exportFilePath - путь до файла, в который будут записываться продукты
 */
data class EntityManagerPriceChangeProperties(
    var enabled: Boolean,
    var cron: String,
    var percent: Float,
    var batchSize: Int,
    var exportFilePath: String,
)
