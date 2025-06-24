
package ru.vassuv.innservice.config

import org.springframework.stereotype.Component

@Component
class UsedValuesRegistry {
    private val usedInns = mutableSetOf<String>()

    @Synchronized
    fun generateUniqueInn(generator: () -> String): String {
        while (true) {
            val candidate = generator()
            if (usedInns.add(candidate)) return candidate
        }
    }
}