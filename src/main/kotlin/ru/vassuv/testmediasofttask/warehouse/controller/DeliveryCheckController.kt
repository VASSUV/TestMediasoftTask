package ru.vassuv.testmediasofttask.warehouse.controller

import org.camunda.bpm.engine.MismatchingMessageCorrelationException
import org.camunda.bpm.engine.RuntimeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/delivery/check")
class DeliveryCheckController(
    private val runtimeService: RuntimeService
) {

    @PostMapping("/result")
    fun submitResult(@RequestBody result: DeliveryCheckResultRequest): ResponseEntity<Void> {
        return try {
            runtimeService
                .createMessageCorrelation("MessageWaitForDeliveryComplete")
                .processInstanceBusinessKey(result.businessKey)
                .correlate()

            println("✅ Проверка доставки обработана: businessKey=${result.businessKey}, ok=${result.isOk}")
            ResponseEntity.ok().build()
        } catch (ex: MismatchingMessageCorrelationException) {
            println("❌ Не удалось отправить результат доставки: ${ex.message}")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}

data class DeliveryCheckResultRequest(
    val businessKey: String,
    val isOk: Boolean
)
