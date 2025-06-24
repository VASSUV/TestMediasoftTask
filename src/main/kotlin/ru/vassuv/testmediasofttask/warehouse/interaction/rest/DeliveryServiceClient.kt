package ru.vassuv.testmediasofttask.warehouse.interaction.rest

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import ru.vassuv.testmediasofttask.warehouse.config.properties.RestServiceProperties
import java.time.LocalDate
import java.util.UUID

interface DeliveryServiceClient {
    fun registerDelivery(orderId: UUID, address: String): DeliveryRegisterResponse
    fun cancelDelivery(deliveryId: UUID)
    fun completeDelivery(deliveryId: UUID)
}

@Component
class DeliveryServiceClientImpl(
    webClientBuilder: WebClient.Builder,
    private val restServiceProperties: RestServiceProperties
) : DeliveryServiceClient {

    private val client = webClientBuilder
        .baseUrl(restServiceProperties.delivery.host)
        .build()

    override fun registerDelivery(orderId: UUID, address: String): DeliveryRegisterResponse {
        return runBlocking {
            client.post()
                .uri(restServiceProperties.delivery.methods.register)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapOf("orderId" to orderId, "address" to address))
                .retrieve()
                .awaitBodyOrNull() ?: error("No delivery response")
        }
    }

    override fun cancelDelivery(deliveryId: UUID) {
        runBlocking {
            client.delete()
                .uri("${restServiceProperties.delivery.methods.register}/$deliveryId")
                .retrieve()
                .toBodilessEntity()
                .awaitFirstOrNull()
                ?: error("Не удалось отменить доставку $deliveryId")
        }
    }

    override fun completeDelivery(deliveryId: UUID) {
        runBlocking {
            client.post() // или patch(), если у тебя PATCH метод
                .uri("${restServiceProperties.delivery.methods.complete}/$deliveryId")
                .retrieve()
                .toBodilessEntity()
                .awaitFirstOrNull()
                ?: error("Не удалось завершить доставку $deliveryId")
        }
    }
}

data class DeliveryRegisterResponse(
    val deliveryId: UUID,
    val expectedDate: LocalDate
)
