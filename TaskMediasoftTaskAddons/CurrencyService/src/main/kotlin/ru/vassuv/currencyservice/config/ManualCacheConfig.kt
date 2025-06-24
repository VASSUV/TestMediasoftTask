package ru.vassuv.currencyservice.config

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ManualCacheConfig {

    @Bean
    fun accountNumberCache(): Cache<String, String> =
        Caffeine.newBuilder()
//            .expireAfterWrite(10, TimeUnit.MINUTES)
//            .maximumSize(1000)
            .build()

    @Bean
    fun innCache(): Cache<String, String> =
        Caffeine.newBuilder()
//            .expireAfterWrite(10, TimeUnit.MINUTES)
//            .maximumSize(1000)
            .build()
}