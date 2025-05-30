package ru.vassuv.testmediasofttask.warehouse.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import ru.vassuv.testmediasofttask.warehouse.exception.ProductNotFoundException
import ru.vassuv.testmediasofttask.warehouse.mock.productEntityMock
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import ru.vassuv.testmediasofttask.warehouse.persist.repository.ProductRepository
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProductServiceTest {

    private val productRepositoryMock = mockk<ProductRepository>() // mock
    private val serviceUnderTest = ProductServiceImpl(productRepositoryMock) // underTest

    @Test
    fun `should return product by id`() {
        val expected = productEntityMock()

        every { productRepositoryMock.findByIdOrNull(expected.id) } returns expected

        val actual = serviceUnderTest.getProductById(expected.id!!)

        assertNotNull(actual)
        assertEquals(expected.name, actual.name)
        assertEquals(expected.article, actual.article)
        assertEquals(expected.description, actual.description)
        assertEquals(expected.category?.name, actual.category?.name)
        assertEquals(expected.price, actual.price)
        assertEquals(expected.quantity, actual.quantity)
        assertEquals(expected.quantityUpdatedAt, actual.quantityUpdatedAt)
        assertEquals(expected.createdAt, actual.createdAt)

        verify { productRepositoryMock.findByIdOrNull(expected.id) }
    }


    @Test
    fun `should throw exception if product not found`() {
        val expectedId = UUID.randomUUID()

        every { productRepositoryMock.findByIdOrNull(expectedId) } returns null

        Assertions.assertThrows(ProductNotFoundException::class.java) {
            serviceUnderTest.getProductById(expectedId)
        }

        verify { productRepositoryMock.findByIdOrNull(expectedId) }
    }
}
