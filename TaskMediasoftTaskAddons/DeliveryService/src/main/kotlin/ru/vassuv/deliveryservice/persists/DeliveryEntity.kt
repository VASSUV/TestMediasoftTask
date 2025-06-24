package ru.vassuv.deliveryservice.persists

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.cglib.core.Local
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "delivery")
data class DeliveryEntity(
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    val id: UUID? = null,
    @Column(nullable = false)
    var orderId: UUID = UUID.randomUUID(),
    @Column(nullable = false)
    var address: String = "",
    @Column(nullable = false)
    var expectedDate: LocalDate = LocalDate.now(),
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: DeliveryStatus = DeliveryStatus.REGISTERED
)


enum class DeliveryStatus {
    REGISTERED,
    COMPLETED,
    CANCELED
}


