package ru.vassuv.testmediasofttask.warehouse.config

import jakarta.persistence.EntityManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.type.AnnotatedTypeMetadata
import org.springframework.scheduling.annotation.EnableScheduling
import ru.vassuv.testmediasofttask.warehouse.config.properties.SchedulingProperties
import ru.vassuv.testmediasofttask.warehouse.scheduling.EntityManagerScheduler
import ru.vassuv.testmediasofttask.warehouse.scheduling.OptimizedPriceScheduler
import ru.vassuv.testmediasofttask.warehouse.scheduling.SimplePriceScheduler

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
    fun simpleScheduler(
        schedulingProperties: SchedulingProperties,
        dataSourceProperties: DataSourceProperties,
    ) = SimplePriceScheduler(schedulingProperties, dataSourceProperties)

    @Bean()
    @ConditionalOnProperty(name = ["scheduling.optimization"], havingValue = "false")
    @Conditional(EntityManagerSchedulingCondition::class)
    fun entityManagerScheduler(
        entityManager: EntityManager,
        schedulingProperties: SchedulingProperties
    ) = EntityManagerScheduler(entityManager, schedulingProperties)

    @Bean
    @Conditional(OptimizedSchedulingCondition::class)
    fun optimizedScheduler(
        schedulingProperties: SchedulingProperties,
        dataSourceProperties: DataSourceProperties,
    ) = OptimizedPriceScheduler(schedulingProperties, dataSourceProperties)
}

/**
 * Условие для проверки на запуск простого шедулера
 */
class SimpleSchedulingCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val env = context.environment
        val schedulingEnabled = env.getProperty("app.scheduling.enabled", Boolean::class.java, false)
        val optimization = env.getProperty("scheduling.optimization", Boolean::class.java, false)
        val entityManagerSchedulingEnable = env.getProperty(
            "scheduling.price-change.entity-manager.enabled",
            Boolean::class.java,
            false
        )
        return schedulingEnabled && !optimization && !entityManagerSchedulingEnable
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

/**
 * Условие для проверки на запуск шедулера работающего на EntityManager
 */
class EntityManagerSchedulingCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val env = context.environment
        val schedulingEnabled = env.getProperty("app.scheduling.enabled", Boolean::class.java, false)
        val optimization = env.getProperty("scheduling.optimization", Boolean::class.java, false)
        val entityManagerSchedulingEnable = env.getProperty(
            "scheduling.price-change.entity-manager.enabled",
            Boolean::class.java,
            false
        )
        return schedulingEnabled && !optimization && entityManagerSchedulingEnable
    }
}
