package ru.vassuv.testmediasofttask.warehouse.config

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.vassuv.testmediasofttask.warehouse.config.properties.S3Properties
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

/**
 * Конфигурация для S3 хранилища
 *
 * @property s3Properties параметры для s3
 */
@Configuration
class S3Config(
    val s3Properties: S3Properties,
) {

    /**
     * Создает бин для [S3Client]
     *
     * @return [S3Client]
     */
    @Bean
    fun s3Client(): S3Client {
        val credentials = StaticCredentialsProvider.create(
            AwsBasicCredentials.create(s3Properties.accessKey, s3Properties.secretKey)
        )

        return S3Client.builder()
            .endpointOverride(URI.create(s3Properties.endpoint)) // обязательно для MinIO
            .credentialsProvider(credentials)
            .region(Region.of(s3Properties.region))
            .forcePathStyle(true) // важно для MinIO!
            .build()
    }
}
