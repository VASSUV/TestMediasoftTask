package ru.vassuv.testmediasofttask.warehouse.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.web.SecurityFilterChain
import java.time.Instant

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
//@EnableMethodSecurity(prePostEnabled = true)
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
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .csrf { it.disable() }
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

    @Bean
    @Primary
    @Suppress("MagicNumber")
    fun jwtDecoder(): JwtDecoder = JwtDecoder { token ->
        Jwt.withTokenValue(token)
            .header("alg", "RS256")
            .claim("sub", "test-user")
            .claim("roles", listOf("USER", "ADMIN", "MANAGER"))
            .claim("profileId", "00000000-0000-0000-0000-000000000000")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build()
    }
}
