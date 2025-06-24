package ru.vassuv.testmediasofttask.warehouse.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import ru.vassuv.testmediasofttask.warehouse.config.properties.CamundaCustomDatasourceProperties
import javax.sql.DataSource

@Configuration
class CamundaDataSourceConfig(
    val camundaCustomDatasourceProperties: CamundaCustomDatasourceProperties,
) {

    @Bean(name = ["camundaBpmDataSource"])
    fun camundaDataSource(): DataSource {
        val ds = HikariDataSource()
        ds.jdbcUrl = camundaCustomDatasourceProperties.url
        ds.username = camundaCustomDatasourceProperties.username
        ds.password = camundaCustomDatasourceProperties.password
        ds.driverClassName = "org.postgresql.Driver"

        return ds
    }

    @Bean
    fun camundaJdbcTemplate(@Qualifier("camundaBpmDataSource") ds: DataSource): JdbcTemplate {
        return JdbcTemplate(ds)
    }
}
