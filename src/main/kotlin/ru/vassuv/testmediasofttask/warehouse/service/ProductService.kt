package ru.vassuv.testmediasofttask.warehouse.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.vassuv.testmediasofttask.warehouse.exception.ProductIsExistWithArticleException
import ru.vassuv.testmediasofttask.warehouse.exception.ProductNotFoundException
import ru.vassuv.testmediasofttask.warehouse.model.domain.CreatedProduct
import ru.vassuv.testmediasofttask.warehouse.repository.ProductRepository
import ru.vassuv.testmediasofttask.warehouse.model.domain.DomainProduct
import ru.vassuv.testmediasofttask.warehouse.model.domain.UpdatedProduct
import ru.vassuv.testmediasofttask.warehouse.service.mappers.applyWith
import ru.vassuv.testmediasofttask.warehouse.service.mappers.toDbo
import ru.vassuv.testmediasofttask.warehouse.service.mappers.toDomain
import java.time.LocalDateTime
import java.util.UUID

@Service
class ProductService(
    private val productRepository: ProductRepository
) {

    fun getProducts(page: Int, size: Int): Page<DomainProduct> {
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        return productRepository.findAll(pageable).map { it.toDomain() }
    }

    fun getProductById(id: UUID): DomainProduct? =
        productRepository.findByIdOrNull(id)?.toDomain() ?: throw ProductNotFoundException(id)

    fun createProduct(product: CreatedProduct): DomainProduct {
        val isExistArticle = productRepository.findByArticle(product.article) != null
        if (isExistArticle) throw ProductIsExistWithArticleException()
        return productRepository.save(product.toDbo(LocalDateTime.now())).toDomain()
    }

    @Transactional
    fun updateProduct(id: UUID, updatedProduct: UpdatedProduct): DomainProduct? {
        val existingProduct = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException(id)
        existingProduct.applyWith(updatedProduct, LocalDateTime.now())
        return productRepository.save(existingProduct).toDomain()
    }

    fun deleteProduct(id: UUID) {
        if (!productRepository.existsById(id)) throw ProductNotFoundException(id)
        productRepository.deleteById(id)
    }
}