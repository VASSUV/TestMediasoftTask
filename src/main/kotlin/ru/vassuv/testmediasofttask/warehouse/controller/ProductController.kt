package ru.vassuv.testmediasofttask.warehouse.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import ru.vassuv.testmediasofttask.warehouse.controller.params.ProductSearchParams
import ru.vassuv.testmediasofttask.warehouse.controller.request.CreateProductRequest
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterRequest
import ru.vassuv.testmediasofttask.warehouse.controller.request.UpdateProductRequest
import ru.vassuv.testmediasofttask.warehouse.controller.response.ProductResponse
import ru.vassuv.testmediasofttask.warehouse.controller.response.UuidResponse
import ru.vassuv.testmediasofttask.warehouse.exception.handler.ApiError
import java.util.*

/**
 * Интерфейс для REST-контроллера управления товарами.
 * Предоставляет CRUD-операции для товаров на складе.
 */

@Tag(
    name = "Products",
    description = "Управление товарами на складе"
)
interface ProductController {

    /**
     * Получение списка товаров с пагинацией.
     *
     * @param pageable Параметры для постраничной загрузки.
     * @return Страница с товарами.
     */
    @Operation(
        summary = "Получение списка товаров",
        description = "Возвращает список всех товаров с пагинацией."
    )
    fun getProducts(pageable: Pageable): Page<ProductResponse>

    /**
     * Получение товара по идентификатору.
     *
     * @param id UUID товара.
     * @return Ответ с товаром или статус 404.
     */
    @Operation(
        summary = "Получение товара",
        description = "Получение одного товара по его ID"
    )
    fun getProductById(@PathVariable id: UUID): Any

    /**
     * Создание нового товара.
     *
     * @param request DTO с данными нового товара.
     * @return Созданный товар.
     */
    @Operation(
        summary = "Создание нового товара",
        description = "Создает новый товар с указанными параметрами.",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [Content(schema = Schema(implementation = CreateProductRequest::class))]
        ),
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Товар успешно создан",
                content = [Content(schema = Schema(implementation = UuidResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Некорректный запрос",
                content = [
                    Content(
                        schema = Schema(implementation = ApiError::class),
                        examples = [
                            ExampleObject(
                                name = "Ошибка валидации",
                                value = """
                                {
                                    "status": 400,
                                    "error": "BAD_REQUEST",
                                    "message": "Не правильно переданное значение в поле: name",
                                    "classPath": "org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor.resolveArgument:159",
                                    "timestamp": "2025-05-28T00:02:29.767701"
                                }
                                """
                            ),
                            ExampleObject(
                                name = "Некорректный JSON",
                                value = """
                                {
                                    "status": 400,
                                    "error": "Malformed JSON request",
                                    "message": "Некорректный формат поля 'category'",
                                    "classPath": "org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.readJavaType:408",
                                    "timestamp": "2025-05-27T23:57:56.657516"
                                }
                                """
                            )
                        ]
                    )
                ]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Конфликт уникальности",
                content = [
                    Content(
                        schema = Schema(implementation = ApiError::class),
                        examples = [
                            ExampleObject(
                                name = "Артикул уже существует",
                                value = """
                                {
                                    "status": 409,
                                    "error": "CONFLICT",
                                    "message": "Товар с таким артикулом уже существует",
                                    "classPath": "ru.vassuv.testmediasofttask.warehouse.service.ProductService.createProduct:62",
                                    "timestamp": "2025-05-28T00:03:59.168566"
                                }
                                """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun createProduct(request: CreateProductRequest): ResponseEntity<UuidResponse>

    /**
     * Обновление товара по идентификатору.
     *
     * @param id UUID товара.
     * @param request DTO с обновленными данными товара.
     * @return статусы 204, 404 или 409.
     */
    @Operation(
        summary = "Обновление существующего товара",
        description = "Обновляет существующий товар с указанными параметрами по его id."
    )
    fun updateProduct(
        id: UUID,
        request: UpdateProductRequest
    ): ResponseEntity<Unit>

    /**
     * Удаление товара по идентификатору.
     *
     * @param id UUID товара.
     * @return статусы 204 или 404.
     */
    @Operation(
        summary = "Удаление существующего товара",
        description = "Удаляет существующий товар по его id."
    )
    fun deleteProduct(id: UUID): ResponseEntity<Unit>

    /**
     * Многокритериальный поиск по продуктам
     *
     * @param params критерии поиска.
     * @param pageable параметры для постаничной закгрузки
     * @return страница продуктов отфильтрованных по критериям
     */
    @Operation(
        summary = "Поиск продуктов по критериям",
        description = "Находит продукты по критериям и собирает страницу"
    )
    fun searchProducts(params: ProductSearchParams, pageable: Pageable): Page<ProductResponse>

    /**
     * Многокритериальный продвинутый поиск по продуктам
     *
     * @param filterRequest критерии поиска.
     * @param pageable параметры для постаничной закгрузки
     * @return страница продуктов отфильтрованных по критериям
     */
    @Operation(
        summary = "Поиск продуктов по критериям",
        description = "Находит продукты по критериям и собирает страницу"
    )
    fun smartSearch(filterRequest: SearchProductFilterRequest, pageable: Pageable): Page<ProductResponse>
}
