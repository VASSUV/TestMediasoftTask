package ru.vassuv.testmediasofttask.warehouse.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import ru.vassuv.testmediasofttask.warehouse.exception.ProductNotFoundException
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.service.model.ProductData
import ru.vassuv.testmediasofttask.warehouse.service.model.UpdatedProduct
import java.util.UUID

/**
 * Сервис для управления товарами на складе.
 *
 * @property productRepository Репозиторий товаров.
 */
interface ProductService {

    /**
     * Получение списка товаров с пагинацией.
     *
     * @param pageable параметры для постранично загрузки
     * @return Страница с товарами в виде Domain-моделей.
     */
    fun getProducts(pageable: Pageable): Page<ProductData>

    /**
     * Получение товара по идентификатору.
     *
     * @param id UUID товара.
     * @return Найденный товар.
     * @throws ProductNotFoundException Если товар не найден.
     */
    fun getProductById(id: UUID): ProductData

    /**
     * Создание нового товара.
     *
     * @param product Доменная модель нового товара.
     * @return Созданный товар.
     */
    fun createProduct(product: CreatedProduct): UUID

    /**
     * Обновление существующего товара.
     *
     * @param id UUID товара.
     * @param updatedProduct Доменная модель с обновленными данными.
     * @return Обновленный товар или null, если не найден.
     */
    fun updateProduct(id: UUID, updatedProduct: UpdatedProduct)

    /**
     * Удаление товара по идентификатору.
     *
     * @param id UUID товара.
     * @return true, если удаление прошло успешно, иначе false.
     */
    fun deleteProduct(id: UUID)

    /**
     * Получение всех товаров (не для API)
     *
     * @return список товаров
     */
    fun findAll(): List<ProductEntity>

    /**
     * Сохранение всех товаров (не для API)
     *
     * @param products список сохраняемых продуктов
     * @return список сохраненных продуктов
     */
    fun saveAll(products: Sequence<ProductEntity>): List<ProductEntity>

    /**
     * Многокритериальный поиск по продуктам
     *
     * @param specification спецификация для поиска продуктов
     * @param pageable параметры для постранично загрузки
     * @return Страница с товарами в виде Domain-моделей.
     */
    fun search(specification: Specification<ProductEntity>, pageable: Pageable): Page<ProductData>
}
