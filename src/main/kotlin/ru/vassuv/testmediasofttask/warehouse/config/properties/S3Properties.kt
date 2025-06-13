package ru.vassuv.testmediasofttask.warehouse.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "s3")
@Suppress("MatchingDeclarationName")
data class S3Properties(
    var endpoint: String,
    var region: String,
    var accessKey: String,
    var secretKey: String,
    var bucket: String,
)