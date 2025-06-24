package ru.vassuv.deliveryservice.persists

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DeliveryRepository : JpaRepository<DeliveryEntity, UUID> {
    fun findByOrderId(orderId: UUID): DeliveryEntity?
}