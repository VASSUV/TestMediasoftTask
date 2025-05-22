package ru.vassuv.testmediasofttask.warehouse.service


import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.vassuv.testmediasofttask.warehouse.model.Product
import ru.vassuv.testmediasofttask.warehouse.repository.ProductRepository
import java.time.LocalDateTime
import java.util.UUID

@Service
class ProductService(
    private val productRepository: ProductRepository
) {

    fun getAllProducts(): List<Product> = productRepository.findAll()

    fun getProductById(id: UUID): Product? =
        productRepository.findByIdOrNull(id)

    fun createProduct(product: Product): Product =
        productRepository.save(product)

    @Transactional
    fun updateProduct(id: UUID, updatedProduct: Product): Product? {
        val existingProduct = productRepository.findByIdOrNull(id) ?: return null

        existingProduct.apply {
            name = updatedProduct.name
            article = updatedProduct.article
            description = updatedProduct.description
            category = updatedProduct.category
            price = updatedProduct.price
            quantity = updatedProduct.quantity
            quantityUpdatedAt = LocalDateTime.now()
        }

        return productRepository.save(existingProduct)
    }

    fun deleteProduct(id: UUID): Boolean {
        if (!productRepository.existsById(id)) return false
        productRepository.deleteById(id)
        return true
    }
}