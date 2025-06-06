package ru.vassuv.testmediasofttask.warehouse.controller.response

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

/**
 * Универсальный DTO, содержащий только UUID.
 *
 * Используется для ответов на создание новых ресурсов, таких как товары, заказы, заказчики и др.
 */
data class UuidResponse(
    /** Уникальный идентификатор созданного ресурса. */
    @field:Schema(description = "Идентификатор товара")
    val id: UUID
)
