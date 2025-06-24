package ru.vassuv.testmediasofttask.warehouse.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "camunda.custom.datasource")
@Suppress("MatchingDeclarationName")
data class CamundaCustomDatasourceProperties(
    var url: String,
    var scheme: String,
    var username: String,
    var password: String, 
)
