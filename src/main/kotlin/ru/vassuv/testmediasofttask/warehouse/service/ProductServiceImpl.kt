package ru.vassuv.testmediasofttask.warehouse.service

import jakarta.persistence.EntityManager
import jakarta.persistence.LockModeType
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.vassuv.testmediasofttask.warehouse.exception.ProductExistsWithArticleException
import ru.vassuv.testmediasofttask.warehouse.exception.ProductNotFoundException
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.persist.repository.ProductRepository
import ru.vassuv.testmediasofttask.warehouse.service.model.ProductData
import ru.vassuv.testmediasofttask.warehouse.service.model.UpdatedProduct
import ru.vassuv.testmediasofttask.warehouse.service.mappers.toProductEntity
import ru.vassuv.testmediasofttask.warehouse.service.mappers.toProductData
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.UUID
import java.util.stream.Stream
import kotlin.streams.asSequence

/**
 * Сервис для управления товарами на складе.
 *
 * @property productRepository Репозиторий товаров.
 */
@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository
): ProductService {

    /**
     * Получение списка товаров с пагинацией.
     *
     * @param pageable параметры для постранично загрузки
     * @return Страница с товарами в виде Domain-моделей.
     */
    override fun getProducts(pageable: Pageable): Page<ProductData> {
        return productRepository.findAll(pageable).map { it.toProductData() }
    }

    /**
     * Получение товара по идентификатору.
     *
     * @param id UUID товара.
     * @return Найденный товар.
     * @throws ProductNotFoundException Если товар не найден.
     */
    override fun getProductById(id: UUID) =
        productRepository.findByIdOrNull(id)?.toProductData() ?: throw ProductNotFoundException(id)

    /**
     * Создание нового товара.
     *
     * @param product Доменная модель нового товара.
     * @return Созданный товар.
     */
    @Transactional
    override fun createProduct(product: CreatedProduct): UUID {
        if (productRepository.existsProductEntityByArticle(product.article)) {
            throw ProductExistsWithArticleException(product.article)
        }

        return productRepository.save(product.toProductEntity()).id!!
    }

    /**
     * Обновление существующего товара.
     *
     * @param id UUID товара.
     * @param updatedProduct Доменная модель с обновленными данными.
     * @return Обновленный товар или null, если не найден.
     */
    @Transactional
    override fun updateProduct(id: UUID, updatedProduct: UpdatedProduct) {
        val result = productRepository.findByIdWithArticleConflict(id, updatedProduct.article)
            ?: throw ProductNotFoundException(id)

        if (result.articleExists) {
            throw ProductExistsWithArticleException(updatedProduct.article)
        }

        val existingProduct = result.product ?: throw ProductNotFoundException(id)
        existingProduct.apply {
            existingProduct.name = updatedProduct.name
            existingProduct.article = updatedProduct.article
            existingProduct.description = updatedProduct.description
            existingProduct.category = updatedProduct.category
            existingProduct.price = updatedProduct.price
            existingProduct.quantity = updatedProduct.quantity
            existingProduct.quantityUpdatedAt = ZonedDateTime.now()
        }
        productRepository.save(existingProduct)
    }

    /**
     * Удаление товара по идентификатору.
     *
     * @param id UUID товара.
     * @return true, если удаление прошло успешно, иначе false.
     */
    @Transactional(readOnly = true)
    override fun deleteProduct(id: UUID) {
        if (!productRepository.existsById(id)) throw ProductNotFoundException(id)
        productRepository.deleteById(id)
    }

    /**
     * Получение всех товаров (не для API)
     *
     * @return список товаров
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    override fun findAll(): List<ProductEntity> =
        productRepository.findAll()

    /**
     * Сохранение всех товаров (не для API)
     *
     * @param products список сохраняемых продуктов
     * @return список сохраненных продуктов
     */
    @Transactional
    override fun saveAll(products: Sequence<ProductEntity>): List<ProductEntity> =
        productRepository.saveAll(products.asIterable())

}
