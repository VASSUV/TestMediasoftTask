package ru.vassuv.testmediasofttask.warehouse.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import ru.vassuv.testmediasofttask.warehouse.model.dbo.ProductDbo
import java.math.BigDecimal
import java.util.UUID

/**
 * Репозиторий для выполнения операций над товарами в базе данных,
 * где за счет интерфеса JPARepository добавляются CRUD операции к БД
 */
interface ProductRepository : JpaRepository<ProductDbo, UUID> {
    fun findByArticle(article: String): ProductDbo?

    @Modifying
    @Query("UPDATE products SET price = price + (price * :percentage)", nativeQuery = true)
    fun updateAllPrices(percentage: BigDecimal): Int
}