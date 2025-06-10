package ru.vassuv.testmediasofttask.warehouse.service.model

import java.util.UUID

/**
 * Доменная модель для создания нового клиента.
 *
 * @property login Логин клиента.
 * @property email Электронная почта клиента.
 * @property profileId Идентификатор профиля заказчика
 * @property isActive Статус активности клиента (по умолчанию true).
 */
data class CreatedCustomer(
    val login: String,
    val email: String,
    val profileId: UUID,
    val isActive: Boolean = true,
)
