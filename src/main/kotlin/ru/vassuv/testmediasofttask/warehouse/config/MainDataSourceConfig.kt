package ru.vassuv.testmediasofttask.warehouse.config

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["ru.vassuv.testmediasofttask.warehouse.persist.repository"],
    entityManagerFactoryRef = "mainEntityManagerFactory",
    transactionManagerRef = "mainTransactionManager"
)
class MainDataSourceConfig(
    val dataSourceProperties: DataSourceProperties,
) {

    @Bean(name = ["dataSource"])
    fun dataSource(): DataSource {
        val ds = HikariDataSource()
        ds.jdbcUrl = dataSourceProperties.url
        ds.username = dataSourceProperties.username
        ds.password = dataSourceProperties.password
        ds.driverClassName = "org.postgresql.Driver"
        return ds
    }

    @Bean(name = ["mainEntityManagerFactory"])
    fun mainEntityManagerFactory(
        @Qualifier("dataSource") dataSource: DataSource
    ): LocalContainerEntityManagerFactoryBean {
        val vendorAdapter = HibernateJpaVendorAdapter()
        val factory = LocalContainerEntityManagerFactoryBean()
        factory.dataSource = dataSource
        factory.setPackagesToScan("ru.vassuv.testmediasofttask.warehouse.persist.entity")
        factory.jpaVendorAdapter = vendorAdapter
        factory.persistenceUnitName = "main"
        val properties = HashMap<String, Any>()
        properties["hibernate.physical_naming_strategy"] =
            "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"
        properties["hibernate.implicit_naming_strategy"] =
            "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy"
        factory.setJpaPropertyMap(properties)
        return factory
    }

    @Bean(name = ["mainTransactionManager"])
    fun transactionManager(
        @Qualifier("mainEntityManagerFactory")
        mainEntityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(mainEntityManagerFactory)
    }
}
