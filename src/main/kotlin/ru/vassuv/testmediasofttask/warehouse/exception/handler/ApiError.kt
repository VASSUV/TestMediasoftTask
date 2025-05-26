package ru.vassuv.testmediasofttask.warehouse.exception.handler

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

/**
 * Класс обертка над ошибкой, возвращаемая приложение при исключениях
 *
 * @property status - статус ответа на запрос TODO но может сделать свой собственный список ошибок
 * @property error - текст ошибки
 * @property message - текст сообщения
 * @property timestamp - время ошибки
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiError(
    val status: Int,
    val error: String,
    val message: String?,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
