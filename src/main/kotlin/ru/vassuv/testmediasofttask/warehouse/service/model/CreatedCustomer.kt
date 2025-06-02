package ru.vassuv.testmediasofttask.warehouse.service.model

/**
 * Доменная модель для создания нового клиента.
 *
 * @property login Логин клиента.
 * @property email Электронная почта клиента.
 * @property isActive Статус активности клиента (по умолчанию true).
 */
data class CreatedCustomer(
    val login: String,
    val email: String,
    val isActive: Boolean = true
)