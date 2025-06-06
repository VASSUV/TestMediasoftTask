package ru.vassuv.testmediasofttask.warehouse.controller.response

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

/**
 * Ответ с идентификатором созданного заказчика.
 *
 * @property id идентификатор нового заказчика.
 */
@Schema(description = "Информация о созданном заказчике")
data class CustomerCreatedResponse(

    @field:Schema(description = "Уникальный идентификатор заказчика")
    val id: UUID
)
