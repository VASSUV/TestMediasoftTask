package ru.vassuv.testmediasofttask.warehouse.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import ru.vassuv.testmediasofttask.warehouse.model.dbo.ProductDbo
import java.math.BigDecimal
import java.util.UUID
import java.util.stream.Stream

/**
 * Репозиторий для выполнения операций над товарами в базе данных,
 * где за счет интерфеса JPARepository добавляются CRUD операции к БД
 */
interface ProductRepository : JpaRepository<ProductDbo, UUID> {
    fun findByArticle(article: String): ProductDbo?

    @Modifying
    @Query("UPDATE products SET price = price + (price * :percentage)", nativeQuery = true)
    fun updateAllPrices(percentage: BigDecimal): Int

    @Query(
        "SELECT * FROM products ORDER BY products.id",
        nativeQuery = true
    )
    @Transactional(readOnly = true)
    fun streamAll(): Stream<ProductDbo>
}