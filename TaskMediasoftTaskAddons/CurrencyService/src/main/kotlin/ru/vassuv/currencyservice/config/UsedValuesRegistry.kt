
package ru.vassuv.currencyservice.config

import org.springframework.stereotype.Component

@Component
class UsedValuesRegistry {
    private val usedInns = mutableSetOf<String>()
    private val usedAccounts = mutableSetOf<String>()

    @Synchronized
    fun generateUniqueInn(generator: () -> String): String {
        while (true) {
            val candidate = generator()
            if (usedInns.add(candidate)) return candidate
        }
    }

    @Synchronized
    fun generateUniqueAccount(generator: () -> String): String {
        while (true) {
            val candidate = generator()
            if (usedAccounts.add(candidate)) return candidate
        }
    }
}