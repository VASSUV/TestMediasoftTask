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

    private val repositoryMock = mockk<ProductRepository>() // mock
    private val serviceUnderTest = ProductService(repositoryMock) // underTest

    @Test
    fun `should return product by id`() {
        val expectedId = UUID.randomUUID() // TODO expectedId
        val expectedName = "Product 1"
        val entity = ProductEntity(
            id = expectedId,
            name = expectedName,
            article = "Art-001",
            description = "Desc",
            category = "Cat",
            price = BigDecimal(100),
            quantity = 10,
            quantityUpdatedAt = LocalDateTime.now(),
            createdAt = LocalDateTime.now()
        )

        every { repositoryMock.findByIdOrNull(expectedId) } returns entity

        val actual = serviceUnderTest.getProductById(expectedId)// TODO actual

        assertNotNull(actual)
        assertEquals(expectedName, actual.name)

        verify(exactly = 1 /*Todo не обязательно */) { repositoryMock.findByIdOrNull(expectedId) }
    }


    @Test
    fun `should throw exception if product not found`() {
        val id = UUID.randomUUID()

        every { repositoryMock.findByIdOrNull(id) } returns null

        assertThrows(ProductNotFoundException::class.java) {
            serviceUnderTest.getProductById(id)
        }

        verify(exactly = 1) { repositoryMock.findByIdOrNull(id) }
    }

}
