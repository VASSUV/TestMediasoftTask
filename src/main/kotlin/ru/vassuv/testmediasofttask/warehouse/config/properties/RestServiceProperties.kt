package ru.vassuv.testmediasofttask.warehouse.config.properties

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "rest")
@Suppress("MatchingDeclarationName")
data class RestServiceProperties(
    var inn: Service<InnMethods>,
    @JsonProperty("account-number")
    var accountNumber: Service<AccountNumberMethods>,
    var currency: Service<CurrencyMethods>
) {


    class Service<T : Any>(
        val mockEnabled: Boolean = false,
        val host: String,
        val methods: T
    )

    class InnMethods(
        val inns: String
    )

    class AccountNumberMethods(
        val accountNumbers: String
    )

    class CurrencyMethods(
        val currencies: String
    )
}
