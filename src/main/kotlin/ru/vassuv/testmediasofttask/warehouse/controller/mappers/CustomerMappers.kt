package ru.vassuv.testmediasofttask.warehouse.controller.mappers

import ru.vassuv.testmediasofttask.warehouse.controller.request.CreateCustomerRequest
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedCustomer

/**
 * Мапперы для преобразования моделей и запросов, связанных с клиентами.
 */

fun CreateCustomerRequest.toCreatedCustomer() = CreatedCustomer(
    login = this.login,
    email = this.email,
    isActive = this.isActive,
)
