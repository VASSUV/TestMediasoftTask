package ru.vassuv.testmediasofttask.warehouse.config

import jakarta.servlet.Filter
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter
import org.camunda.bpm.spring.boot.starter.configuration.CamundaMetricsConfiguration
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
open class CamundaConfig {

    @Bean
    open fun disableMetrics(): CamundaMetricsConfiguration = object : CamundaMetricsConfiguration {
        override fun preInit(processEngineConfiguration: ProcessEngineConfigurationImpl?) {
            super.preInit(processEngineConfiguration)
            processEngineConfiguration?.isMetricsEnabled = false
        }
    }


    @Bean
    @Suppress("MagicNumber")
    open fun configureEngineAuthenticationFilter(): FilterRegistrationBean<Filter?> {
        val filter: FilterRegistrationBean<Filter?> = FilterRegistrationBean<Filter?>()

        filter.setFilter(ProcessEngineAuthenticationFilter())
        filter.setName("camunda-auth")
        filter.addInitParameter(
            "authentication-provider",
            "org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider"
        )
        filter.addUrlPatterns("/engine-rest/*")
        filter.order = 101

        return filter
    }

    @Bean
    open fun processCorsFilter(): FilterRegistrationBean<Filter?> {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration()

        corsConfiguration.allowCredentials = true
        corsConfiguration.addAllowedOriginPattern("*")
        corsConfiguration.addAllowedHeader("*")
        corsConfiguration.addAllowedMethod("*")

        source.registerCorsConfiguration("/**", corsConfiguration)
        val filter: FilterRegistrationBean<Filter?> = FilterRegistrationBean<Filter?>(CorsFilter(source))
        filter.order = 0

        return filter
    }
}

