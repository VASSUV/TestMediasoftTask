
package ru.vassuv.deliveryservice.service

import org.springframework.stereotype.Service
import ru.vassuv.deliveryservice.persists.DeliveryEntity
import ru.vassuv.deliveryservice.persists.DeliveryRepository
import ru.vassuv.deliveryservice.persists.DeliveryStatus
import ru.vassuv.deliveryservice.service.model.RegisterDeliveryResult
import java.time.LocalDate
import java.util.UUID


@Service
class DeliveryService(
    private val repository: DeliveryRepository
) {
    fun registerDelivery(orderId: UUID, address: String): RegisterDeliveryResult {
        val existing = repository.findByOrderId(orderId)
        if (existing != null) return RegisterDeliveryResult(
            deliverId = existing.id!!,
            expectedDate = existing.expectedDate
        )

        val expected = LocalDate.now().plusDays(2)
        val delivery = repository.save(DeliveryEntity(orderId = orderId, address = address, expectedDate = expected))
        return RegisterDeliveryResult(
            deliverId = delivery.id!!,
            expectedDate = delivery.expectedDate
        )
    }

    fun completeDelivery(deliveryId: UUID) {
        val delivery = repository.findById(deliveryId)
            .orElseThrow { IllegalArgumentException("Доставка $deliveryId не найдена") }

        delivery.status = DeliveryStatus.COMPLETED
        repository.save(delivery)
    }

    fun cancelDelivery(deliveryId: UUID) {
        val delivery = repository.findById(deliveryId)
            .orElseThrow { IllegalArgumentException("Доставка $deliveryId не найдена") }

        delivery.status = DeliveryStatus.CANCELED
        repository.save(delivery)
    }
}