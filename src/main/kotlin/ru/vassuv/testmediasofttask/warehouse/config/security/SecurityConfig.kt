package ru.vassuv.testmediasofttask.warehouse.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

/**
 * Конфигурация [SecurityConfig].
 *
 * Используется для настройки фильтрации по токену.
 *
 * @property jwtAuthenticationConverter
 *
 * @see [org.springframework.boot.autoconfigure.kafka.KafkaProperties]
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class SecurityConfig(
    val jwtAuthenticationConverter: CustomJwtAuthenticationConverter
) {

    /**
     * Bean securityFilterChain
     *
     * @param http [org.springframework.security.config.annotation.web.builders.HttpSecurity]
     * @return [org.springframework.security.web.SecurityFilterChain]
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .oauth2ResourceServer { configurer ->
                configurer.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)
                }
            }
            .build()
    }

    /**
     * Bean passwordEncoder
     *
     * @return [org.springframework.security.crypto.password.PasswordEncoder]
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
