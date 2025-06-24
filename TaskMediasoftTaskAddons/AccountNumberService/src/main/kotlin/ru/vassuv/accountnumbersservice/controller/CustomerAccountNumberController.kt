package ru.vassuv.accountnumbersservice.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.vassuv.accountnumbersservice.service.AccountNumberService

@RestController
@RequestMapping("/api/account-numbers")
class CustomerAccountNumberController(
    private val accountNumberService: AccountNumberService
) {

    @PostMapping
    fun getAccountNumbers(@RequestBody logins: List<String>): Map<String, String> =
        accountNumberService.getAccountNumbers(logins)
}
