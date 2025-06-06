package ru.vassuv.testmediasofttask.warehouse.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

/**
 * Конфигурация кэширования с использованием Caffeine.
 *
 * Используется для кратковременного хранения курсов валют.
 *
 * @see [Caffeine]
 */
@Configuration
class CacheConfig {

    /**
     * Создаёт и настраивает менеджер кэша [CaffeineCacheManager].
     *
     * Кэширование значений происходит на 1 минуту после записи.
     */
    @Bean
    fun cacheManager(): CacheManager {
        val caffeineCacheManager = CaffeineCacheManager("currencies")
        caffeineCacheManager.setCaffeine(
            Caffeine.newBuilder()
                //.maximumSize(1000) // TODO добавить ограничение по размеру
                .expireAfterWrite(Duration.ofMinutes(1))
        )
        return caffeineCacheManager
    }
}