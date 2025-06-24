package ru.vassuv.innservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InnServiceApplication

fun main(args: Array<String>) {
    runApplication<InnServiceApplication>(*args)
}
