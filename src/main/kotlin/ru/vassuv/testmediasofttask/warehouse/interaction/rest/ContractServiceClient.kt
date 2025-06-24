package ru.vassuv.testmediasofttask.warehouse.interaction.rest

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import ru.vassuv.testmediasofttask.warehouse.config.properties.RestServiceProperties
import java.util.UUID

interface ContractServiceClient {
    fun registerContract(inn: String, accountNumber: String): UUID
    fun enableContract(contractId: UUID)
    fun cancelContract(contractId: UUID)
}

@Component
class ContractServiceClientImpl(
    webClientBuilder: WebClient.Builder,
    private val restServiceProperties: RestServiceProperties
) : ContractServiceClient {

    private val client = webClientBuilder
        .baseUrl(restServiceProperties.contract.host)
        .build()

    override fun registerContract(inn: String, accountNumber: String): UUID {
        return runBlocking {
            client.post()
                .uri(restServiceProperties.contract.methods.contract)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapOf("inn" to inn, "accountNumber" to accountNumber))
                .retrieve()
                .awaitBodyOrNull<ContractRegisterResponse>()
                ?.contractId
                ?: error("Empty contractId from contract-service")
        }
    }

    override fun enableContract(contractId: UUID) {
        runBlocking {
            client.post()
                .uri("${restServiceProperties.contract.methods.contractEnable}/$contractId")
                .retrieve()
                .toBodilessEntity()
                .awaitFirstOrNull()
                ?: error("Не удалось сделать договор $contractId активным")
        }
    }

    override fun cancelContract(contractId: UUID) {
        runBlocking {
            client.delete()
                .uri("${restServiceProperties.contract.methods.contractEnable}/$contractId")
                .retrieve()
                .toBodilessEntity()
                .awaitFirstOrNull()
                ?: error("Не удалось сделать договор $contractId активным")
        }
    }
}

data class ContractRegisterResponse(
    val contractId: UUID
)

