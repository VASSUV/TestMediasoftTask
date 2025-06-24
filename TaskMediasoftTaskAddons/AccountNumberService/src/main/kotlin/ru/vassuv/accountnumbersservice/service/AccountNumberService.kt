
package ru.vassuv.accountnumbersservice.service

import com.github.benmanes.caffeine.cache.Cache
import org.springframework.stereotype.Service
import ru.vassuv.accountnumbersservice.config.UsedValuesRegistry
import ru.vassuv.accountnumbersservice.config.ValueGenerator

@Service
class AccountNumberService(
    private val accountNumberCache: Cache<String, String>,
    private val usedValuesRegistry: UsedValuesRegistry,
    private val valueGenerator: ValueGenerator,
) {
    fun getAccountNumbers(logins: List<String>): Map<String, String> {
        return logins.associateWith { login ->
            accountNumberCache.get(login) {
                usedValuesRegistry.generateUniqueAccount {
                    valueGenerator.generateAccountNumber()
                }
            }
        }
    }
}