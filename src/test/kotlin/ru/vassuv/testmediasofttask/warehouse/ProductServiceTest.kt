package ru.vassuv.testmediasofttask.warehouse

import ru.vassuv.testmediasofttask.warehouse.persist.repository.ProductRepository
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import ru.vassuv.testmediasofttask.warehouse.exception.ProductNotFoundException
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProductServiceTest {

    private val repository = mockk<ProductRepository>()
    private val service = ProductService(repository)

    @Test
    fun `should return product by id`() {
        val id = UUID.randomUUID()
        val entity = ProductEntity(
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
