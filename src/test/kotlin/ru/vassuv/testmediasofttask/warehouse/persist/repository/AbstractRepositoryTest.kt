package ru.vassuv.testmediasofttask.warehouse.persist.repository

import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(TestcontainersConfig::class)
@SqlGroup(
    Sql(
        scripts = ["/init_product_database_started_products.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    ),
    Sql(
        scripts = ["/truncate_product_database.sql"],
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
)
abstract class AbstractRepositoryTest {
    @Autowired
    protected lateinit var productRepository: ProductRepository
}

@Configuration
open class TestcontainersConfig {

    @Container
    val postgres = PostgreSQLContainer("postgres:15").apply {
        withDatabaseName("test-db")
        withUsername("test")
        withPassword("test")
    }

    @DynamicPropertySource
    fun overrideProps(registry: DynamicPropertyRegistry) {
        registry.add("spring.datasource.url") { postgres.jdbcUrl }
        registry.add("spring.datasource.username") { postgres.username }
        registry.add("spring.datasource.password") { postgres.password }
        registry.add("spring.jpa.hibernate.ddl-auto") { "update" } // если нужно
    }
}
