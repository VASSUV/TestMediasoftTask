package ru.vassuv.currencyservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CurrencyServiceApplication

fun main(args: Array<String>) {
    runApplication<CurrencyServiceApplication>(*args)
}
