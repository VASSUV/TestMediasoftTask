package ru.vassuv.testmediasofttask.warehouse.camunda

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.contractIdVar
import ru.vassuv.testmediasofttask.warehouse.interaction.rest.ContractServiceClient

@Component("enableContractDelegate")
class EnableContractDelegate(
    private val contractServiceClient: ContractServiceClient
) : JavaDelegate {
    override fun execute(execution: DelegateExecution) {
        val contractId = execution.contractIdVar
        contractServiceClient.enableContract(contractId)
        println("❌ Контракт $contractId активирован")
    }
}
