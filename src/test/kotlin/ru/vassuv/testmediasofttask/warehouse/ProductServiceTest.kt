package ru.vassuv.testmediasofttask.warehouse

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import ru.vassuv.testmediasofttask.warehouse.repository.ProductRepository
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import ru.vassuv.testmediasofttask.warehouse.exception.ProductNotFoundException
import ru.vassuv.testmediasofttask.warehouse.model.dbo.ProductDbo
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
class ProductServiceTest {

    private val repository = mockk<ProductRepository>()
    private val service = ProductService(repository)

    @Test
    fun `should return product by id`() {
        val id = UUID.randomUUID()
        val entity = ProductDbo(
            id = id,
            name = "Product 1",
            article = "Art-001",
            description = "Desc",
            category = "Cat",
            price = BigDecimal(100),
            quantity = 10,
            quantityUpdatedAt = LocalDateTime.now(),
            createdAt = LocalDateTime.now()
        )

        every { repository.findByIdOrNull(id) } returns entity

        val result = service.getProductById(id)

        assertNotNull(result)
        assertEquals("Product 1", result?.name)

        verify(exactly = 1) { repository.findByIdOrNull(id) }
    }


    @Test
    fun `should throw exception if product not found`() {
        val id = UUID.randomUUID()

        every { repository.findByIdOrNull(id) } returns null

        assertThrows(ProductNotFoundException::class.java) {
            service.getProductById(id)
        }

        verify(exactly = 1) { repository.findByIdOrNull(id) }
    }

}