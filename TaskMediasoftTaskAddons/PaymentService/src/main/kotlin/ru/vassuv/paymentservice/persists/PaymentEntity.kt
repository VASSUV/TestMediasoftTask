package ru.vassuv.paymentservice.persists

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "payment")
data class PaymentEntity(
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    val id: UUID? = null,

    @Column(nullable = false)
    val orderId: UUID,

    @Column(nullable = false)
    val accountNumber: String,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Column(nullable = false)
    val success: Boolean,

    @CreationTimestamp
    @Column(nullable = false)
    val createdAt: ZonedDateTime = ZonedDateTime.now()
)