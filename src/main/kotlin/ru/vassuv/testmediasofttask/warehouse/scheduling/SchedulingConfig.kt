package ru.vassuv.testmediasofttask.warehouse.scheduling

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.type.AnnotatedTypeMetadata
import org.springframework.scheduling.annotation.EnableScheduling
import ru.vassuv.testmediasofttask.warehouse.service.ProductService

@Configuration
@EnableScheduling
@Profile("!local") // не создаются в профиле local
class SchedulerConfig {

    @Bean
    @ConditionalOnProperty(name = ["scheduling.optimization"], havingValue = "false")
    @Conditional(SimpleSchedulingCondition::class)
    fun simpleScheduler(productService: ProductService) = SimplePriceScheduler(productService)

    @Bean("optimizedScheduler")
    @Conditional(OptimizedSchedulingCondition::class)
    fun optimizedScheduler(productService: ProductService) = OptimizedPriceScheduler(productService)
}

class SimpleSchedulingCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val env = context.environment
        val schedulingEnabled = env.getProperty("app.scheduling.enabled", Boolean::class.java, false)
        val optimization = env.getProperty("scheduling.optimization", Boolean::class.java, false)
        return schedulingEnabled && !optimization
    }
}

class OptimizedSchedulingCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val env = context.environment
        val schedulingEnabled = env.getProperty("app.scheduling.enabled", Boolean::class.java, false)
        val optimization = env.getProperty("scheduling.optimization", Boolean::class.java, false)
        return schedulingEnabled && optimization
    }
}