package ru.vassuv.accountnumbersservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AccountNumbersServiceApplication

fun main(args: Array<String>) {
    runApplication<AccountNumbersServiceApplication>(*args)
}
