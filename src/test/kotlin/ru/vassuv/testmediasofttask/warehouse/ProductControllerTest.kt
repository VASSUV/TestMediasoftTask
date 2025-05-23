package ru.vassuv.testmediasofttask.warehouse

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.vassuv.testmediasofttask.warehouse.controller.ProductController
import ru.vassuv.testmediasofttask.warehouse.model.domain.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.model.domain.DomainProduct
import ru.vassuv.testmediasofttask.warehouse.model.dto.CreateProductRequestDto
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test

/**
 * Интеграционные тесты REST-контроллера для управления товарами.
 */
@WebMvcTest(ProductController::class)
class ProductControllerTest(
    @Autowired val mockMvc: MockMvc
) {
    @MockkBean
    lateinit var productService: ProductService

    @Test
    fun `should create new product`() {
        val requestDto = CreateProductRequestDto("Name", "Article-123", null, null, BigDecimal(100), 10)
        val domainProduct = CreatedProduct("Name", "Article-123", null, null, BigDecimal(100), 10)
        val createdProduct = DomainProduct(
            UUID.randomUUID(),
            "Name",
            "Article-123",
            null,
            null,
            BigDecimal(100),
            10,
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        every { productService.createProduct(domainProduct) } returns createdProduct

        mockMvc.post("/api/products") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(requestDto)
        }.andExpect {
            status { isCreated() }
            jsonPath("$.name") { value("Name") }
            jsonPath("$.article") { value("Article-123") }
        }
    }

    @Test
    fun `should return validation errors when creating invalid product`() {
        val invalidDto = CreateProductRequestDto("", "", null, null, BigDecimal(-1), -5)

        mockMvc.post("/api/products") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(invalidDto)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.name") { exists() }
            jsonPath("$.article") { exists() }
            jsonPath("$.price") { exists() }
            jsonPath("$.quantity") { exists() }
        }
    }
}