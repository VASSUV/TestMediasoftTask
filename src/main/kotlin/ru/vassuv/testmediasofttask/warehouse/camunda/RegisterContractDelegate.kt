package ru.vassuv.testmediasofttask.warehouse.camunda

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.accountNumberVar
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.innVar
import ru.vassuv.testmediasofttask.warehouse.interaction.rest.ContractServiceClient
import ru.vassuv.testmediasofttask.warehouse.camunda.extension.CONTRACT_ID

@Component("registerContractDelegate")
class RegisterContractDelegate(
    private val contractServiceClient: ContractServiceClient
) : JavaDelegate {
    override fun execute(execution: DelegateExecution) {
        val inn = execution.innVar
        val accountNumber = execution.accountNumberVar
        val contractId = contractServiceClient.registerContract(inn, accountNumber)

        execution.setVariable(CONTRACT_ID, contractId.toString())
        println("✅ Договор зарегистрирован: $contractId")
    }
}
