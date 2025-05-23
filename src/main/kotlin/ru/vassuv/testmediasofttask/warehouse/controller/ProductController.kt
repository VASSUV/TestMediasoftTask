package ru.vassuv.testmediasofttask.warehouse.controller

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toDomain
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toResponseDto
import ru.vassuv.testmediasofttask.warehouse.model.dto.CreateProductRequestDto
import ru.vassuv.testmediasofttask.warehouse.model.dto.ProductResponseDto
import ru.vassuv.testmediasofttask.warehouse.model.dto.UpdateProductRequestDto
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import java.util.UUID

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    fun getProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Page<ProductResponseDto> =
        productService.getProducts(page, size).map { it.toResponseDto() }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: UUID): ResponseEntity<ProductResponseDto> =
        productService.getProductById(id)
            ?.toResponseDto()
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @PostMapping
    fun createProduct(@Valid @RequestBody request: CreateProductRequestDto): ResponseEntity<ProductResponseDto> =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(productService.createProduct(request.toDomain()).toResponseDto())

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateProductRequestDto
    ): ResponseEntity<ProductResponseDto> =
        productService.updateProduct(id, request.toDomain())
            ?.toResponseDto()
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: UUID): ResponseEntity<Unit> {
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }
}