package ru.vassuv.testmediasofttask.warehouse.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import ru.vassuv.testmediasofttask.warehouse.model.dbo.ProductDbo
import java.util.UUID

interface ProductRepository : JpaRepository<ProductDbo, UUID> {
    fun findByArticle(article: String): ProductDbo?
}