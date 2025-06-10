package ru.vassuv.testmediasofttask.warehouse.config.security

import org.springframework.security.access.AccessDeniedException
import java.util.UUID

/**
 * Параметры из токена авторизации
 */
sealed interface WarehousePrincipal {

    /**
     * Получает profileId иначе вызывает исключение
     *
     * @return id профиля
     *
     * @throws если требует ProfileId а его нет, то исключение [org.springframework.security.access.AccessDeniedException]
     */
    fun getUserProfileIdOrError(): UUID = (this as? User)?.profileId
        ?: throw AccessDeniedException("Access denied for non-user principal")

    /**
     * Параметры из токена обычного пользователя
     *
     * @property sub
     * @property profileId
     * @property roles
     */
    data class User(
        val sub: String,
        val profileId: UUID,
        val roles: List<String>,
    ) : WarehousePrincipal

    /**
     * Параметры из токена сервисного пользователя
     *
     * @property scope
     */
    data class Scope(
        val scope: String
    ) : WarehousePrincipal
}
