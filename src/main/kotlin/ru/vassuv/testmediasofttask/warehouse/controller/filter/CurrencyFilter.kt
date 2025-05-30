package ru.vassuv.testmediasofttask.warehouse.controller.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.OncePerRequestFilter
import ru.vassuv.testmediasofttask.warehouse.enums.CurrencyType

@Component
class CurrencyFilter(private val currencySession: CurrencySession) : OncePerRequestFilter() {
    companion object {
        const val CURRENCY_HEADER = "Currency"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val headerCurrency = request.getHeader(CURRENCY_HEADER)
            ?.let { currency -> CurrencyType.entries.firstOrNull { it.name == currency } }

        currencySession.setCurrency(headerCurrency)

        filterChain.doFilter(request, response)
    }
}

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
class CurrencySession {
    private var currency: CurrencyType? = null

    fun setCurrency(currency: CurrencyType?) {
        this.currency = currency ?: return
    }

    fun getCurrency(): CurrencyType {
        return currency ?: CurrencyType.RUB
    }
}