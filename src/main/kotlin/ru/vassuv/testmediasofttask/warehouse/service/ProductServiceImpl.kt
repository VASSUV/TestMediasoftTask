package ru.vassuv.testmediasofttask.warehouse.service

import jakarta.persistence.LockModeType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager
import ru.vassuv.testmediasofttask.warehouse.exception.ProductExistsWithArticleException
import ru.vassuv.testmediasofttask.warehouse.exception.ProductNotFoundException
import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.KafkaProducer
import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.KafkaEvent
import ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event.KafkaTopic
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import ru.vassuv.testmediasofttask.warehouse.persist.repository.ProductImageRepository
import ru.vassuv.testmediasofttask.warehouse.persist.repository.ProductRepository
import ru.vassuv.testmediasofttask.warehouse.service.model.mappers.toProductData
import ru.vassuv.testmediasofttask.warehouse.service.model.mappers.toProductEntity
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.service.model.ProductData
import ru.vassuv.testmediasofttask.warehouse.service.model.UpdatedProduct
import java.time.ZonedDateTime
import java.util.*

/**
 * Реализация сервиса управления товарами ([ProductService]).
 *
 * Реализует бизнес-логику управления товарами на складе.
 *
 * @property productRepository репозиторий товаров ([ProductRepository]).
 * @property productImageRepository репозиторий товаров ([ProductImageRepository]).
 */
@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productImageRepository: ProductImageRepository,
    private val kafkaProducer: KafkaProducer
) : ProductService {

    /**
     * Получает постраничный список товаров.
     *
     * @param pageable параметры пагинации и сортировки.
     * @return страница с товарами, представленными в виде доменных моделей ([ProductData]).
     */
    override fun getProducts(pageable: Pageable): Page<ProductData> =
        productRepository.findAll(pageable).map { it.toProductData() }

    /**
     * Получает товар по его идентификатору.
     *
     * @param id уникальный идентификатор товара.
     * @return товар в виде доменной модели ([ProductData]).
     *
     * @throws [ProductNotFoundException] если товар не найден.
     */
    override fun getProductById(id: UUID): ProductData =
        productRepository.findByIdOrNull(id)?.toProductData()
            ?: throw ProductNotFoundException(id)

    /**
     * Создаёт новый товар.
     *
     * @param product модель создаваемого товара ([CreatedProduct]).
     * @return идентификатор созданного товара.
     *
     * @throws [ProductExistsWithArticleException] если товар с таким артикулом уже существует.
     */
    @Transactional
    override fun createProduct(product: CreatedProduct): UUID {
        if (productRepository.existsProductEntityByArticle(product.article)) {
            throw ProductExistsWithArticleException(product.article)
        }

        return productRepository.save(product.toProductEntity()).id!!
    }

    /**
     * Обновляет существующий товар.
     *
     * @param id идентификатор товара.
     * @param updatedProduct обновленные данные товара ([UpdatedProduct]).
     *
     * @throws [ProductNotFoundException] если товар не найден.
     * @throws [ProductExistsWithArticleException] если новый артикул уже используется другим товаром.
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
            name = updatedProduct.name
            article = updatedProduct.article
            description = updatedProduct.description
            category = updatedProduct.category
            price = updatedProduct.price
            quantity = updatedProduct.quantity
            quantityUpdatedAt = ZonedDateTime.now()
        }
        productRepository.save(existingProduct)
    }

    /**
     * Удаляет товар по его идентификатору.
     *
     * @param id идентификатор товара.
     *
     * @throws [ProductNotFoundException] если товар не найден.
     */
    @Transactional()
    override fun deleteProduct(id: UUID) {
        if (!productRepository.existsById(id)) throw ProductNotFoundException(id)
        productRepository.deleteById(id)
        val images = productImageRepository.findAllByProductId(id)
        productImageRepository.deleteAllByProductId(id)
        TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
            override fun afterCommit() {
                kafkaProducer.sendEvent(
                    KafkaTopic.DELETE_PRODUCT_IMAGE,
                    KafkaEvent.Product.DeleteImage(id, images.map { it.s3Key })
                )
            }
        })
    }

    /**
     * Получает список всех товаров с блокировкой на запись.
     *
     * Используется в транзакциях и шедулерах, не доступен через API.
     *
     * @return список товаров ([ProductEntity]).
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    override fun findAll(): List<ProductEntity> =
        productRepository.findAll()

    /**
     * Сохраняет список товаров.
     *
     * Используется для пакетных операций (batch) и шедулеров.
     *
     * @param products последовательность товаров для сохранения ([ProductEntity]).
     * @return список сохраненных товаров.
     */
    @Transactional
    override fun saveAll(products: Sequence<ProductEntity>): List<ProductEntity> =
        productRepository.saveAll(products.asIterable())

    /**
     * Многокритериальный поиск товаров по спецификации.
     *
     * @param specification спецификация с критериями поиска ([Specification]).
     * @param pageable параметры пагинации.
     * @return страница найденных товаров ([ProductData]).
     */
    override fun search(specification: Specification<ProductEntity>, pageable: Pageable): Page<ProductData> =
        productRepository.findAll(specification, pageable).map { it.toProductData() }
}
