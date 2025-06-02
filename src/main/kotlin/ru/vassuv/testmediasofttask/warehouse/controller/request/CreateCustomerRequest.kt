package ru.vassuv.testmediasofttask.warehouse.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import ru.vassuv.testmediasofttask.warehouse.controller.CustomerController
import ru.vassuv.testmediasofttask.warehouse.persist.entity.CustomerEntity

/**
 * DTO для создания нового заказчика ([CustomerEntity]).
 *
 * Используется при регистрации новых заказчиков через API ([CustomerController.createCustomer]).
 */
data class CreateCustomerRequest(
    /** Уникальный логин заказчика для авторизации. Обязательное поле. */
    @field:NotBlank(message = "Логин заказчика обязателен")
    @field:Schema(description = "Логин заказчика", example = "john_doe")
    val login: String,

    /** Уникальный email заказчика. Обязательное поле. */
    @field:NotBlank(message = "Email заказчика обязателен")
    @field:Email(message = "Email должен быть корректным")
    @field:Schema(description = "Email заказчика", example = "john@example.com")
    val email: String,

    /** Флаг активности заказчика. По умолчанию true. */
    @field:Schema(description = "Активен ли заказчик", example = "true", defaultValue = "true")
    val isActive: Boolean = true
)
