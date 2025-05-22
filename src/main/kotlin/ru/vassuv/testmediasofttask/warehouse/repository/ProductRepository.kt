package ru.vassuv.testmediasofttask.warehouse.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.vassuv.testmediasofttask.warehouse.model.Product
import java.util.UUID

interface ProductRepository : JpaRepository<Product, UUID> {
    fun findByArticle(article: String): Product?
}