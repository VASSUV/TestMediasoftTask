package ru.vassuv.testmediasofttask.warehouse.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.vassuv.testmediasofttask.warehouse.exception.ProductIsExistWithArticleException
import ru.vassuv.testmediasofttask.warehouse.exception.ProductNotFoundException
import ru.vassuv.testmediasofttask.warehouse.model.dbo.ProductDbo
import ru.vassuv.testmediasofttask.warehouse.model.domain.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.repository.ProductRepository
import ru.vassuv.testmediasofttask.warehouse.model.domain.DomainProduct
import ru.vassuv.testmediasofttask.warehouse.model.domain.UpdatedProduct
import ru.vassuv.testmediasofttask.warehouse.service.mappers.applyWith
import ru.vassuv.testmediasofttask.warehouse.service.mappers.toDbo
import ru.vassuv.testmediasofttask.warehouse.service.mappers.toDomain
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import kotlin.streams.asSequence

/**
 * Сервис для управления товарами на складе.
 *
 * @property productRepository Репозиторий товаров.
 */
@Service
class ProductService(
    private val productRepository: ProductRepository
) {

    /**
     * Получение списка товаров с пагинацией.
     *
     * @param page Номер страницы.
     * @param size Количество элементов на странице.
     * @return Страница с товарами в виде Domain-моделей.
     */
    fun getProducts(page: Int, size: Int): Page<DomainProduct> {
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        return productRepository.findAll(pageable).map { it.toDomain() }
    }

    /**
     * Получение товара по идентификатору.
     *
     * @param id UUID товара.
     * @return Найденный товар.
     * @throws ProductNotFoundException Если товар не найден.
     */
    fun getProductById(id: UUID): DomainProduct? =
        productRepository.findByIdOrNull(id)?.toDomain() ?: throw ProductNotFoundException(id)

    /**
     * Создание нового товара.
     *
     * @param product Доменная модель нового товара.
     * @return Созданный товар.
     */
    @Transactional
    fun createProduct(product: CreatedProduct): DomainProduct {
        val isExistArticle = productRepository.findByArticle(product.article) != null // TODO заменить на existsByArticle, для эффективности
        if (isExistArticle)
            throw ProductIsExistWithArticleException()
        return productRepository.save(product.toDbo(LocalDateTime.now())).toDomain()
    }

    /**
     * Обновление существующего товара.
     *
     * @param id UUID товара.
     * @param updatedProduct Доменная модель с обновленными данными.
     * @return Обновленный товар или null, если не найден.
     */
    @Transactional
    fun updateProduct(id: UUID, updatedProduct: UpdatedProduct): DomainProduct? {
        val existingProduct = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException(id)
        existingProduct.applyWith(updatedProduct, LocalDateTime.now())
        return productRepository.save(existingProduct).toDomain()
    }

    /**
     * Удаление товара по идентификатору.
     *
     * @param id UUID товара.
     * @return true, если удаление прошло успешно, иначе false.
     */
    fun deleteProduct(id: UUID) {
        if (!productRepository.existsById(id)) throw ProductNotFoundException(id)
        productRepository.deleteById(id)
    }

    /**
     * Получение всех товаров (не для API)
     *
     * @return список товаров
     */
    fun findAll(): List<ProductDbo> =
        productRepository.findAll()

    /**
     * Сохранение всех товаров (не для API)
     *
     * @param products список сохраняемых продуктов
     * @return список сохраненных продуктов
     */
    @Transactional
    fun saveAll(products: Sequence<ProductDbo>): List<ProductDbo> =
        productRepository.saveAll(products.asIterable())

    /**
     * Обновление цены всех товаров на определенную процентную величину
     *
     * @param percentage - величина изменения цены в процентах
     */
    @Transactional
    fun updateAllPrices(percentage: BigDecimal) {
        productRepository.updateAllPrices(percentage)
    }

    /**
     * Обновление цены все товаров батчами
     *
     * @param percent - величина изменения цены в процентах
     * @param batchSize - размер батча
     * @param batchConsumer - колбэк для постобработки батча
     */
    @Transactional
    fun updatePricesInBatches(
        percent: BigDecimal,
        batchSize: Int,
        batchConsumer: (List<ProductDbo>) -> Unit
    ) {
        productRepository.streamAll().use { stream ->
            stream.asSequence().chunked(batchSize).forEach { batch ->
                batch.forEach { it.price *= (BigDecimal.ONE + percent) }
                productRepository.saveAll(batch)
                batchConsumer(batch)
            }
        }
    }
}