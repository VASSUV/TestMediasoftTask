package ru.vassuv.testmediasofttask.warehouse.controller.response

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

/**
 * DTO-ответ с uuid
 */
data class UuidResponse(
    @field:Schema(description = "Идентификатор товара")
    val id: UUID,
)
