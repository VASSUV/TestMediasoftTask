package ru.vassuv.testmediasofttask.warehouse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class WarehouseApplication

fun main(args: Array<String>) {
	runApplication<WarehouseApplication>(args = args)
}
