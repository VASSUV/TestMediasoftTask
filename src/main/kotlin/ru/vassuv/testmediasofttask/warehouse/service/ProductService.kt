package ru.vassuv.testmediasofttask.warehouse.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import ru.vassuv.testmediasofttask.warehouse.exception.ProductNotFoundException
import ru.vassuv.testmediasofttask.warehouse.exception.ProductExistsWithArticleException
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.service.model.ProductData
import ru.vassuv.testmediasofttask.warehouse.service.model.UpdatedProduct
import java.util.UUID

/**
 * Интерфейс сервиса управления товарами на складе.
 *
 * Реализует бизнес-логику операций с товарами.
 */
interface ProductService {

    /**
     * Получает список товаров с пагинацией.
     *
     * @param pageable параметры пагинации и сортировки.
     * @return страница с товарами, представленными в виде доменных моделей [ProductData].
     */
    fun getProducts(pageable: Pageable): Page<ProductData>

    /**
     * Получает товар по его идентификатору.
     *
     * @param id уникальный идентификатор товара.
     * @return найденный товар ([ProductData]).
     *
     * @throws [ProductNotFoundException] если товар не найден.
     */
    fun getProductById(id: UUID): ProductData

    /**
     * Создаёт новый товар на основе переданной модели.
     *
     * @param product модель нового товара ([CreatedProduct]).
     * @return идентификатор созданного товара.
     *
     * @throws ProductExistsWithArticleException если товар с таким артикулом уже существует.
     */
    fun createProduct(product: CreatedProduct): UUID

    /**
     * Обновляет существующий товар.
     *
     * @param id идентификатор товара.
     * @param updatedProduct модель с обновлёнными данными ([UpdatedProduct]).
     *
     * @throws [ProductNotFoundException] если товар не найден.
     * @throws ProductExistsWithArticleException если новый артикул уже используется другим товаром.
     */
    fun updateProduct(id: UUID, updatedProduct: UpdatedProduct)

    /**
     * Удаляет товар по указанному идентификатору.
     *
     * @param id идентификатор товара.
     *
     * @throws [ProductNotFoundException] если товар не найден.
     */
    fun deleteProduct(id: UUID)

    /**
     * Возвращает список всех товаров.
     *
     * Используется во внутренних процессах и операциях, не предоставляется через API.
     *
     * @return список сущностей товаров ([ProductEntity]).
     */
    fun findAll(): List<ProductEntity>

    /**
     * Сохраняет список товаров.
     *
     * Используется в шедулерах и batch-операциях, не предоставляется через API.
     *
     * @param products последовательность товаров ([ProductEntity]) для сохранения.
     * @return список сохранённых товаров.
     */
    fun saveAll(products: Sequence<ProductEntity>): List<ProductEntity>

    /**
     * Выполняет многокритериальный поиск товаров по спецификации.
     *
     * @param specification критерии поиска товаров ([Specification]).
     * @param pageable параметры пагинации и сортировки.
     * @return страница найденных товаров ([ProductData]).
     */
    fun search(specification: Specification<ProductEntity>, pageable: Pageable): Page<ProductData>
}
