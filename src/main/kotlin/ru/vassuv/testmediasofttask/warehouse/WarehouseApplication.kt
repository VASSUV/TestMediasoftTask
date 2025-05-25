package ru.vassuv.testmediasofttask.warehouse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.vassuv.testmediasofttask.warehouse.scheduling.config.OptimizedPriceChangeProperties
import ru.vassuv.testmediasofttask.warehouse.scheduling.config.SimplePriceChangeProperties

@EnableConfigurationProperties(
	SimplePriceChangeProperties::class,
	OptimizedPriceChangeProperties::class
)
@SpringBootApplication
class WarehouseApplication

fun main(args: Array<String>) {
	runApplication<WarehouseApplication>(*args)
}
