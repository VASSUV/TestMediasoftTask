package ru.vassuv.contractservice.persists

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ContractRepository : JpaRepository<ContractEntity, UUID> {
    fun findByInnAndAccountNumber(inn: String, accountNumber: String): ContractEntity?
}