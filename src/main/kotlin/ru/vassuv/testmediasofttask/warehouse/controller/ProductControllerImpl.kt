package ru.vassuv.testmediasofttask.warehouse.controller

import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toCreatedProduct
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toProductResponse
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toUpdatedProduct
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toUuidResponse
import ru.vassuv.testmediasofttask.warehouse.controller.params.ProductSearchParams
import ru.vassuv.testmediasofttask.warehouse.controller.request.CreateProductRequest
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterRequest
import ru.vassuv.testmediasofttask.warehouse.controller.request.UpdateProductRequest
import ru.vassuv.testmediasofttask.warehouse.controller.response.ProductResponse
import ru.vassuv.testmediasofttask.warehouse.controller.response.UuidResponse
import ru.vassuv.testmediasofttask.warehouse.controller.specification.ProductSearchSpecification.fromCriteria
import ru.vassuv.testmediasofttask.warehouse.controller.specification.ProductSearchSpecification.fromSmartCriteria
import ru.vassuv.testmediasofttask.warehouse.enums.CurrencyType
import ru.vassuv.testmediasofttask.warehouse.exception.ProductExistsWithArticleException
import ru.vassuv.testmediasofttask.warehouse.exception.ProductNotFoundException
import ru.vassuv.testmediasofttask.warehouse.service.CurrencyConversionService
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import java.util.*

/**
 * Реализация REST-контроллера для управления товарами.
 *
 * Предоставляет API-методы для CRUD-операций и различных вариантов поиска товаров.
 *
 * @property productService Сервис для выполнения операций с товарами ([ProductService]).
 * @property currencyConversionService Сервис для получения и применения курсов валют ([CurrencyConversionService]).
 */
@RestController
@RequestMapping("/api/products")
class ProductControllerImpl(
    private val productService: ProductService,
    private val currencyConversionService: CurrencyConversionService
) : ProductController {

    /**
     * Возвращает список товаров с пагинацией.
     *
     * @param pageable параметры пагинации и сортировки.
     * @return страница объектов [ProductResponse],
     * цены которых конвертированы в текущую валюту сессии ([CurrencyType]).
     */
    @GetMapping
    override fun getProducts(pageable: Pageable) =
        currencyConversionService.getExchangeRateInfo()
            .let { rateInfo ->
                productService.getProducts(pageable)
                    .map { it.toProductResponse(rateInfo) }
            }

    /**
     * Возвращает информацию о товаре по его идентификатору.
     *
     * @param id уникальный идентификатор товара.
     * @return объект [ProductResponse] с информацией о товаре.
     *
     * @throws ProductNotFoundException если товар с указанным идентификатором не найден.
     */
    @GetMapping("/{id}")
    override fun getProductById(@PathVariable id: UUID) =
        currencyConversionService.getExchangeRateInfo()
            .let { rateInfo ->
                productService.getProductById(id).toProductResponse(rateInfo)
            }

    /**
     * Создаёт новый товар.
     *
     * @param request данные товара ([CreateProductRequest]).
     * @return HTTP-ответ с идентификатором созданного товара ([UuidResponse]) и статусом CREATED.
     *
     * @throws ProductExistsWithArticleException если товар с указанным артикулом уже существует.
     * @throws jakarta.validation.ValidationException при некорректных данных.
     */
    @PostMapping
    override fun createProduct(@Valid @RequestBody request: CreateProductRequest) =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(productService.createProduct(request.toCreatedProduct()).toUuidResponse())

    /**
     * Обновляет данные существующего товара.
     *
     * @param id идентификатор товара.
     * @param request обновлённые данные товара ([UpdateProductRequest]).
     * @return HTTP-ответ со статусом NO_CONTENT.
     *
     * @throws ProductNotFoundException если товар не найден.
     * @throws ProductExistsWithArticleException если артикул уже занят.
     * @throws jakarta.validation.ValidationException при некорректных данных.
     */
    @PutMapping("/{id}")
    override fun updateProduct(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateProductRequest
    ) = ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(productService.updateProduct(id, request.toUpdatedProduct()))

    /**
     * Удаляет товар.
     *
     * @param id идентификатор товара.
     * @return HTTP-ответ со статусом NO_CONTENT.
     *
     * @throws ProductNotFoundException если товар не найден.
     */
    @DeleteMapping("/{id}")
    override fun deleteProduct(@PathVariable id: UUID) =
        ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(productService.deleteProduct(id))

    /**
     * Обычный многокритериальный поиск по товарам.
     *
     * @param params критерии поиска ([ProductSearchParams]).
     * @param pageable параметры пагинации.
     * @return Страница найденных товаров ([ProductResponse]).
     */
    @GetMapping("/search")
    override fun searchProducts(
        @ModelAttribute params: ProductSearchParams,
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ) = currencyConversionService.getExchangeRateInfo()
        .let { rateInfo ->
            productService.search(fromCriteria(params), pageable)
                .map { it.toProductResponse(rateInfo) }
        }

    /**
     * Продвинутый многокритериальный поиск товаров с вложенными условиями.
     *
     * @param filterRequest сложные условия поиска ([SearchProductFilterRequest]).
     * @param pageable параметры пагинации.
     * @return Страница найденных товаров ([ProductResponse]).
     */
    @PostMapping("/search/smart")
    override fun smartSearch(
        @Valid @RequestBody filterRequest: SearchProductFilterRequest,
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ) = currencyConversionService.getExchangeRateInfo()
        .let { rateInfo ->
            productService.search(fromSmartCriteria(filterRequest), pageable)
                .map { it.toProductResponse(rateInfo) }
        }
}
