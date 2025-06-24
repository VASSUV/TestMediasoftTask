
package ru.vassuv.innservice.service

import com.github.benmanes.caffeine.cache.Cache
import org.springframework.stereotype.Service
import ru.vassuv.innservice.config.UsedValuesRegistry
import ru.vassuv.innservice.config.ValueGenerator

@Service
class InnService(
    private val innCache: Cache<String, String>,
    private val usedValuesRegistry: UsedValuesRegistry,
    private val valueGenerator: ValueGenerator,
) {
    fun getInns(logins: List<String>): Map<String, String> {
        return logins.associateWith { login ->
            innCache.get(login) { usedValuesRegistry.generateUniqueInn { valueGenerator.generateInn() } }
        }
    }
}