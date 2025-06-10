package ru.vassuv.testmediasofttask.warehouse.config.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import java.util.*

/**
 * Конвертер JWT токена в абстрактный токен spring
 *
 * @see [Jwt]
 * @see [AbstractAuthenticationToken]
 */
@Component
class CustomJwtAuthenticationConverter : Converter<Jwt, AbstractAuthenticationToken> {

    /**
     * Конвертирует JWT токен в абстрактный токен spring
     *
     * @param jwt
     * @return [AbstractAuthenticationToken]
     */
    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val roles = extractRoles(jwt)
        val scope = jwt.getClaimAsString("scope")
        val authorities = roles + scope?.let { SimpleGrantedAuthority(it) }
        val auth = UsernamePasswordAuthenticationToken(
            if (scope != null) {
                WarehousePrincipal.Scope(scope)
            } else {
                WarehousePrincipal.User(
                    sub = jwt.getClaimAsString("sub"),
                    profileId = jwt.getClaimAsString("profileId").let(UUID::fromString),
                    roles = roles.map { it.authority }
                )
            },
            "N/A",
            authorities.filterNotNull()
        )
        auth.details = mapOf<String, String>()
        return auth
    }

    /**
     * Извлекает из [Jwt] список ролей
     *
     * @param jwt
     * @return List<[SimpleGrantedAuthority]>
     */
    private fun extractRoles(jwt: Jwt): Collection<GrantedAuthority> {
        val roles = jwt.getClaimAsStringList("roles") ?: listOf()
        return roles.map { SimpleGrantedAuthority("ROLE_$it") }
    }
}
