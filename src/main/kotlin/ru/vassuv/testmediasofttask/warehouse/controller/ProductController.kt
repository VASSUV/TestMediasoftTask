package ru.vassuv.testmediasofttask.warehouse.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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

/**
 * REST-контроллер для управления товарами.
 * Предоставляет CRUD-операции для товаров на складе.
 *
 * @property productService
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Управление товарами на складе")
class ProductController(
    private val productService: ProductService
) {

    /**
     * Получение списка товаров с пагинацией.
     *
     * @param page Номер страницы (начинается с 0).
     * @param size Количество элементов на странице.
     * @return Страница с товарами.
     */
    @GetMapping
    @Operation(summary = "Получение списка товаров", description = "Возвращает список всех товаров с пагинацией.")
    fun getProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Page<ProductResponseDto> =
        productService.getProducts(page, size).map { it.toResponseDto() }

    /**
     * Получение товара по идентификатору.
     *
     * @param id UUID товара.
     * @return Ответ с товаром или статус 404.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение товара", description = "Получение одного товара по его ID")
    fun getProductById(@PathVariable id: UUID): ResponseEntity<ProductResponseDto> =
        productService.getProductById(id)
            ?.toResponseDto()
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    /**
     * Создание нового товара.
     *
     * @param request DTO с данными нового товара.
     * @return Созданный товар.
     */
    @PostMapping
    @Operation(summary = "Создание нового товара", description = "Создает новый товар с указанными параметрами.")
    fun createProduct(@Valid @RequestBody request: CreateProductRequestDto): ResponseEntity<ProductResponseDto> =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(productService.createProduct(request.toDomain()).toResponseDto())

    /**
     * Обновление товара по идентификатору.
     *
     * @param id UUID товара.
     * @param request DTO с обновленными данными товара.
     * @return Обновленный товар или статус 404.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновление существующего товара", description = "Обновляет существующий товар с указанными параметрами по его id.")
    fun updateProduct(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateProductRequestDto
    ): ResponseEntity<ProductResponseDto> =
        productService.updateProduct(id, request.toDomain())
            ?.toResponseDto()
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    /**
     * Удаление товара по идентификатору.
     *
     * @param id UUID товара.
     * @return Статус успешного удаления (204) или 404.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление существующего товара", description = "Удаляет существующий товар по его id.")
    fun deleteProduct(@PathVariable id: UUID): ResponseEntity<Unit> {
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }
}