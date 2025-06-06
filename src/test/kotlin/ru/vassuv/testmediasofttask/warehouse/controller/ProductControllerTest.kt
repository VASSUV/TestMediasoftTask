package ru.vassuv.testmediasofttask.warehouse.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import ru.vassuv.testmediasofttask.warehouse.controller.request.CreateProductRequest
import ru.vassuv.testmediasofttask.warehouse.exception.ProductExistsWithArticleException
import ru.vassuv.testmediasofttask.warehouse.exception.handler.GlobalExceptionHandler
import ru.vassuv.testmediasofttask.warehouse.mock.createProductRequestMock
import ru.vassuv.testmediasofttask.warehouse.service.CurrencyConversionService
import ru.vassuv.testmediasofttask.warehouse.service.CurrencyProviderService
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import java.util.*
import kotlin.test.Test

/**
 * Интеграционные тесты REST-контроллера для управления товарами.
 */
class ProductControllerTest {
    private val productServiceMock = mockk<ProductService>()
    private val currencyServiceMock = mockk<CurrencyConversionService>()
    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(
        ProductControllerImpl(productServiceMock, currencyServiceMock)
    )
        .setControllerAdvice(GlobalExceptionHandler()).build()


    @Test
    fun `should create new product`() {
        val expectedId = UUID.randomUUID()

        every { productServiceMock.createProduct(any()) } returns expectedId

        mockMvc.post("/api/products") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString( createProductRequestMock())
        }.andExpect {
            status { isCreated() }
            jsonPath("$.id") { value(expectedId.toString()) }
        }
    }

    @Test
    fun `should return validation errors when creating invalid product`() {
        val invalidCreatedProductRequest = createProductRequestMock(name = "")

        mockMvc.post("/api/products") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(invalidCreatedProductRequest)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.status") { value(400) }
            jsonPath("$.error") { value("BAD_REQUEST") }
        }
    }

    @Test
    fun `should return validation errors when creating product with exists article`() {
        val invalidCreatedProductRequest = createProductRequestMock()

        every { productServiceMock.createProduct(any()) } throws ProductExistsWithArticleException("")

        mockMvc.post("/api/products") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(invalidCreatedProductRequest)
        }.andExpect {
            status { isConflict() }
            jsonPath("$.status") { value(409) }
            jsonPath("$.error") { value("CONFLICT") }
        }
    }
}
