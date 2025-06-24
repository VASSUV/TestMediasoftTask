package ru.vassuv.currencyservice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.vassuv.currencyservice.service.CurrencyService
import java.math.BigDecimal

@RestController
@RequestMapping("/api/currencies")
class CurrencyController(private val currencyService: CurrencyService) {

    @GetMapping
    fun getCurrencies(): Map<String, BigDecimal> {
        return currencyService.getCurrencies()
    }
}