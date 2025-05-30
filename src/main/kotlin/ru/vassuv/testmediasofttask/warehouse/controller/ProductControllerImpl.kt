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
import ru.vassuv.testmediasofttask.warehouse.controller.specification.ProductSearchSpecification.fromCriteria
import ru.vassuv.testmediasofttask.warehouse.controller.specification.ProductSearchSpecification.fromSmartCriteria
import ru.vassuv.testmediasofttask.warehouse.service.CurrencyConversionService
import ru.vassuv.testmediasofttask.warehouse.service.ProductService
import java.util.*

/**
 * REST-контроллер для управления товарами.
 * Предоставляет CRUD-операции для товаров на складе.
 *
 * @property productService
 */
@RestController
@RequestMapping("/api/products")
class ProductControllerImpl(
    private val productService: ProductService,
    private val currencyConversionService: CurrencyConversionService
) : ProductController {

    /**
     * Получение списка товаров с пагинацией.
     *
     * @param pageable Параметры для постраничной загрузки
     * @return Страница с товарами.
     */
    @GetMapping
    override fun getProducts(pageable: Pageable) = currencyConversionService.getExchangeRateInfo()
        .let { rateInfo ->
            productService.getProducts(pageable).map { it.toProductResponse(rateInfo) }
        }


    /**
     * Получение товара по идентификатору.
     *
     * @param id UUID товара.
     * @return Ответ с товаром или статус 404.
     */
    @GetMapping("/{id}")
    override fun getProductById(@PathVariable id: UUID) = currencyConversionService.getExchangeRateInfo()
        .let { rateInfo ->
            productService.getProductById(id).toProductResponse(rateInfo)
        }

    /**
     * Создание нового товара.
     *
     * @param request DTO с данными нового товара.
     * @return Созданный товар.
     */
    @PostMapping
    override fun createProduct(@Valid @RequestBody request: CreateProductRequest) = ResponseEntity
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
    override fun updateProduct(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateProductRequest
    ) = ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(productService.updateProduct(id, request.toUpdatedProduct()))

    /**
     * Удаление товара по идентификатору.
     *
     * @param id UUID товара.
     */
    @DeleteMapping("/{id}")
    override fun deleteProduct(@PathVariable id: UUID) = ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(productService.deleteProduct(id))

    /**
     * Многокритериальный поиск по продуктам
     *
     * @param params критерии поиска.
     * @param pageable параметры для постаничной закгрузки
     * @return страница продуктов отфильтрованных по критериям
     */
    @GetMapping("/search")
    override fun searchProducts(
        @ModelAttribute params: ProductSearchParams,
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ) = currencyConversionService.getExchangeRateInfo().let { rateInfo ->
        productService
            .search(fromCriteria(params), pageable)
            .map { it.toProductResponse(rateInfo) }
    }

    /**
     * Многокритериальный продвинутый поиск по продуктам
     *
     * @param filterRequest критерии поиска.
     * @param pageable параметры для постаничной закгрузки
     * @return страница продуктов отфильтрованных по критериям
     */
    @PostMapping("/search/smart")
    override fun smartSearch(
        @Valid @RequestBody
        filterRequest: SearchProductFilterRequest,
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ) = currencyConversionService.getExchangeRateInfo().let { rateInfo ->
        productService
            .search(fromSmartCriteria(filterRequest), pageable)
            .map { it.toProductResponse(rateInfo) }
    }
}
