
package ru.vassuv.contractservice.service

import org.springframework.stereotype.Service
import ru.vassuv.contractservice.persists.ContractEntity
import ru.vassuv.contractservice.persists.ContractRepository
import ru.vassuv.contractservice.persists.ContractStatus
import java.util.UUID

@Service
class ContractService(
    private val repository: ContractRepository
) {
    fun register(inn: String, accountNumber: String): UUID {
        return repository.save(
            ContractEntity(inn = inn, accountNumber = accountNumber)
        ).id!!
    }

    fun cancelContract(contractId: UUID) {
        val contract = repository.findById(contractId)
            .orElseThrow { IllegalArgumentException("Договор $contractId не найден") }

        contract.status = ContractStatus.CANCELED
        repository.save(contract)
    }

    fun enableContract(contractId: UUID) {
        val contract = repository.findById(contractId)
            .orElseThrow { IllegalArgumentException("Договор $contractId подтвержден") }

        contract.status = ContractStatus.ENABLED
        repository.save(contract)
    }
}