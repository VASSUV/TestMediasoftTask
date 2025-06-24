package ru.vassuv.testmediasofttask.warehouse.interaction.rest

import kotlinx.coroutines.runBlocking
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import ru.vassuv.testmediasofttask.warehouse.config.properties.RestServiceProperties
import java.math.BigDecimal
import java.util.UUID

interface PaymentServiceClient {
    fun makePayment(orderId: UUID, accountNumber: String, amount: BigDecimal): Boolean
}

@Component
class PaymentServiceClientImpl(
    webClientBuilder: WebClient.Builder,
    private val restServiceProperties: RestServiceProperties
) : PaymentServiceClient {

    private val client = webClientBuilder
        .baseUrl(restServiceProperties.payment.host)
        .build()

    override fun makePayment(orderId: UUID, accountNumber: String, amount: BigDecimal): Boolean {
        return runBlocking {
            client.post()
                .uri(restServiceProperties.payment.methods.pay) // например, "/api/payment"
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapOf(
                    "orderId" to orderId,
                    "accountNumber" to accountNumber,
                    "amount" to amount
                ))
                .retrieve()
                .awaitBodyOrNull<PaymentResponse>()
                ?.success
                ?: false
        }
    }
}

data class PaymentResponse(
    val success: Boolean
)
