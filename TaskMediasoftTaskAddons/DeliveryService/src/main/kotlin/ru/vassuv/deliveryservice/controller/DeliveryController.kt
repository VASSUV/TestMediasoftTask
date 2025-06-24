package ru.vassuv.deliveryservice.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.vassuv.deliveryservice.controller.model.RegisterDeliveryRequest
import ru.vassuv.deliveryservice.controller.model.RegisterDeliveryResponse
import ru.vassuv.deliveryservice.service.DeliveryService
import java.util.UUID

@RestController
@RequestMapping("/api")
class DeliveryController(
    private val service: DeliveryService,
) {

    @PostMapping("/register")
    fun register(@RequestBody req: RegisterDeliveryRequest): RegisterDeliveryResponse {
        val deliveryResult = service.registerDelivery(req.orderId, req.address)
        return RegisterDeliveryResponse(deliveryResult.deliverId, deliveryResult.expectedDate)
    }

    @PostMapping("/delivery/complete/{deliveryId}")
    fun completeDelivery(@PathVariable deliveryId: UUID): ResponseEntity<Void> {
        service.completeDelivery(deliveryId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/register/{deliveryId}")
    fun cancelDelivery(@PathVariable deliveryId: UUID): ResponseEntity<Void> {
        service.cancelDelivery(deliveryId)
        return ResponseEntity.noContent().build()
    }
}