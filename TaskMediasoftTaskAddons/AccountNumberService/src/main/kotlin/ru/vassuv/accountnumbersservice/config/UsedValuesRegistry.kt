
package ru.vassuv.accountnumbersservice.config

import org.springframework.stereotype.Component

@Component
class UsedValuesRegistry {
    private val usedAccounts = mutableSetOf<String>()

    @Synchronized
    fun generateUniqueAccount(generator: () -> String): String {
        while (true) {
            val candidate = generator()
            if (usedAccounts.add(candidate)) return candidate
        }
    }
}