package ru.vassuv.testmediasofttask.warehouse.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.vassuv.testmediasofttask.warehouse.model.Product
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import java.util.UUID

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    fun getAllProducts(): List<Product> =
        productService.getAllProducts()

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: UUID): ResponseEntity<Product> =
        productService.getProductById(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @PostMapping
    fun createProduct(@RequestBody product: Product): ResponseEntity<Product> =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(productService.createProduct(product))

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: UUID,
        @RequestBody product: Product
    ): ResponseEntity<Product> =
        productService.updateProduct(id, product)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: UUID): ResponseEntity<Unit> =
        if (productService.deleteProduct(id))
            ResponseEntity.noContent().build()
        else
            ResponseEntity.notFound().build()
}