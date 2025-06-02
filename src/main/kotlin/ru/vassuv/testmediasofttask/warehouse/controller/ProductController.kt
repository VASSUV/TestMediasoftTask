package ru.vassuv.testmediasofttask.warehouse.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.ValidationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import ru.vassuv.testmediasofttask.warehouse.controller.params.ProductSearchParams
import ru.vassuv.testmediasofttask.warehouse.controller.request.CreateProductRequest
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterRequest
import ru.vassuv.testmediasofttask.warehouse.controller.request.UpdateProductRequest
import ru.vassuv.testmediasofttask.warehouse.controller.response.ProductResponse
import ru.vassuv.testmediasofttask.warehouse.controller.response.UuidResponse
import ru.vassuv.testmediasofttask.warehouse.exception.ProductExistsWithArticleException
import ru.vassuv.testmediasofttask.warehouse.exception.ProductNotFoundException
import java.util.*

/**
 * Интерфейс REST-контроллера для управления товарами.
 *
 * Предоставляет операции для создания, чтения, обновления и удаления (CRUD),
 * а также различные виды поиска и фильтрации товаров на складе.
 */
@Tag(
    name = "Products",
    description = "Управление товарами на складе"
)
interface ProductController {

    /**
     * Возвращает постраничный список всех товаров.
     *
     * @param pageable параметры пагинации и сортировки.
     * @return Страница с информацией о товарах.
     */
    @Operation(
        summary = "Получение списка товаров",
        description = "Возвращает список всех товаров с пагинацией."
    )
    fun getProducts(pageable: Pageable): Page<ProductResponse>

    /**
     * Возвращает товар по указанному UUID.
     *
     * @param id уникальный идентификатор товара.
     * @return объект товара, если найден; иначе 404 статус.
     *
     * @throws ProductNotFoundException если товар не найден.
     */
    @Operation(
        summary = "Получение товара по идентификатору",
        description = "Возвращает данные одного товара по его идентификатору."
    )
    fun getProductById(@PathVariable id: UUID): Any

    /**
     * Создаёт новый товар.
     *
     * Проверяет уникальность артикула и корректность данных.
     *
     * @param request данные нового товара.
     * @return Ответ со статусом CREATED и идентификатором товара.
     *
     * @throws ProductExistsWithArticleException если товар с таким артикулом уже существует.
     * @throws ValidationException при некорректных входных данных.
     */
    @Operation(
        summary = "Создание нового товара",
        description = "Создаёт новый товар с указанными параметрами."
    )
    fun createProduct(request: CreateProductRequest): ResponseEntity<UuidResponse>

    /**
     * Обновляет данные существующего товара.
     *
     * @param id уникальный идентификатор товара.
     * @param request обновлённые данные товара.
     * @return статус NO_CONTENT при успешном обновлении, 404 если товар не найден, 409 если конфликт данных.
     *
     * @throws ProductNotFoundException если товар не найден.
     * @throws ProductExistsWithArticleException если возник конфликт уникальности артикула.
     * @throws ValidationException при некорректных входных данных.
     */
    @Operation(
        summary = "Обновление существующего товара",
        description = "Обновляет данные товара по его идентификатору."
    )
    fun updateProduct(
        id: UUID,
        request: UpdateProductRequest
    ): ResponseEntity<Unit>

    /**
     * Удаляет существующий товар.
     *
     * @param id уникальный идентификатор товара.
     * @return статус NO_CONTENT при успешном удалении, 404 если товар не найден.
     *
     * @throws ProductNotFoundException если товар не найден.
     */
    @Operation(
        summary = "Удаление товара",
        description = "Удаляет товар по указанному идентификатору."
    )
    fun deleteProduct(id: UUID): ResponseEntity<Unit>

    /**
     * Многокритериальный поиск товаров.
     *
     * @param params параметры поиска (фильтры).
     * @param pageable параметры пагинации.
     * @return Страница товаров, соответствующих критериям поиска.
     */
    @Operation(
        summary = "Поиск товаров по критериям",
        description = "Производит поиск товаров по набору критериев."
    )
    fun searchProducts(params: ProductSearchParams, pageable: Pageable): Page<ProductResponse>

    /**
     * Продвинутый многокритериальный поиск товаров с поддержкой вложенных условий.
     *
     * @param filterRequest объект с критериями сложного поиска.
     * @param pageable параметры пагинации.
     * @return Страница товаров, соответствующих сложным критериям поиска.
     */
    @Operation(
        summary = "Продвинутый поиск товаров",
        description = "Выполняет сложный поиск товаров с возможностью комбинирования условий."
    )
    fun smartSearch(filterRequest: SearchProductFilterRequest, pageable: Pageable): Page<ProductResponse>
}