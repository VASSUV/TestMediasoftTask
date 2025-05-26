package ru.vassuv.testmediasofttask.warehouse.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toUpdatedProduct
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toResponseDto
import ru.vassuv.testmediasofttask.warehouse.controller.request.CreateProductRequest
import ru.vassuv.testmediasofttask.warehouse.controller.response.ProductResponse
import ru.vassuv.testmediasofttask.warehouse.controller.request.UpdateProductRequest
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toCreatedProduct
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toUuidResponse
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
     * @param pageable Параметры для постраничной загрузки
     * @return Страница с товарами.
     */
    @GetMapping
    @Operation(summary = "Получение списка товаров", description = "Возвращает список всех товаров с пагинацией.")
    fun getProducts(pageable: Pageable) = productService.getProducts(pageable).map { it.toResponseDto() }

    /**
     * Получение товара по идентификатору.
     *
     * @param id UUID товара.
     * @return Ответ с товаром или статус 404.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение товара", description = "Получение одного товара по его ID")
    fun getProductById(@PathVariable id: UUID) = productService.getProductById(id).toResponseDto()

    /**
     * Создание нового товара.
     *
     * @param request DTO с данными нового товара.
     * @return Созданный товар.
     */
    @PostMapping
    @Operation(summary = "Создание нового товара", description = "Создает новый товар с указанными параметрами.")
    fun createProduct(@Valid @RequestBody request: CreateProductRequest) = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(productService.createProduct(request.toCreatedProduct()).toUuidResponse())

    /**
     * Обновление товара по идентификатору.
     *
     * @param id UUID товара.
     * @param request DTO с обновленными данными товара.
     * @return Обновленный товар или статус 404.
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Обновление существующего товара",
        description = "Обновляет существующий товар с указанными параметрами по его id."
    )
    fun updateProduct(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateProductRequest
    ) = productService.updateProduct(id, request.toUpdatedProduct()) // TODO 200 -> 204

    /**
     * Удаление товара по идентификатору.
     *
     * @param id UUID товара.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление существующего товара", description = "Удаляет существующий товар по его id.")
    fun deleteProduct(@PathVariable id: UUID) = productService.deleteProduct(id) // TODO 200 -> 204
}
