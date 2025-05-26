package ru.vassuv.testmediasofttask.warehouse.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.type.AnnotatedTypeMetadata
import org.springframework.scheduling.annotation.EnableScheduling
import ru.vassuv.testmediasofttask.warehouse.config.properties.OptimizedPriceChangeProperties
import ru.vassuv.testmediasofttask.warehouse.config.properties.SchedulingProperties
import ru.vassuv.testmediasofttask.warehouse.config.properties.SimplePriceChangeProperties
import ru.vassuv.testmediasofttask.warehouse.scheduling.OptimizedPriceScheduler
import ru.vassuv.testmediasofttask.warehouse.scheduling.SimplePriceScheduler
import ru.vassuv.testmediasofttask.warehouse.service.ProductExportService
import ru.vassuv.testmediasofttask.warehouse.service.ProductService

/**
 * Конфигурация с шедулерами, не работает в local профиле
 */
@Configuration
@EnableScheduling
@Profile("!local") // не создаются в профиле local
class SchedulerConfig {

    @Bean
    @ConditionalOnProperty(name = ["scheduling.optimization"], havingValue = "false")
    @Conditional(SimpleSchedulingCondition::class)
    fun simpleScheduler(properties: SchedulingProperties, productService: ProductService) =
        SimplePriceScheduler(properties, productService)

    @Bean("optimizedScheduler")
    @Conditional(OptimizedSchedulingCondition::class)
    fun optimizedScheduler(
        properties: SchedulingProperties,
        productService: ProductService,
        productExportService: ProductExportService,
    ) = OptimizedPriceScheduler(properties, productService, productExportService)
}

/**
 * Условие для проверки на запуск простого шедулера
 */
class SimpleSchedulingCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val env = context.environment
        val schedulingEnabled = env.getProperty("app.scheduling.enabled", Boolean::class.java, false)
        val optimization = env.getProperty("scheduling.optimization", Boolean::class.java, false)
        return schedulingEnabled && !optimization
    }
}

/**
 * Условие для проверки на запуск сложного, оптимизированного шедулера
 */
class OptimizedSchedulingCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val env = context.environment
        val schedulingEnabled = env.getProperty("app.scheduling.enabled", Boolean::class.java, false)
        val optimization = env.getProperty("scheduling.optimization", Boolean::class.java, false)
        return schedulingEnabled && optimization
    }
}
