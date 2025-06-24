package ru.vassuv.contractservice.persists

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "contract")
data class ContractEntity(
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    val id: UUID? = null,

    @Column(nullable = false)
    var inn: String = "",

    @Column(nullable = false)
    var accountNumber: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ContractStatus = ContractStatus.CREATED
)

enum class ContractStatus {
    CREATED,
    ENABLED,
    CANCELED
}

