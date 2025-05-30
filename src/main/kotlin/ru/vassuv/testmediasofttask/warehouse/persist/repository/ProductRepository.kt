package ru.vassuv.testmediasofttask.warehouse.persist.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductWithConflictCheck
import java.math.BigDecimal
import java.util.UUID
import java.util.stream.Stream

/**
 * Репозиторий для выполнения операций над товарами в базе данных,
 * где за счет интерфеса JPARepository добавляются CRUD операции к БД
 */
interface ProductRepository : JpaRepository<ProductEntity, UUID> {
    fun existsProductEntityByArticle(article: String): Boolean

    @Query("""
        SELECT p as product, 
            EXISTS(
                SELECT 1 FROM ProductEntity p2 
                WHERE p2.article = :article AND p2.id <> :id
            ) AS articleExists
        FROM ProductEntity p
        WHERE p.id = :id
    """)
    fun findByIdWithArticleConflict(id: UUID, article: String): ProductWithConflictCheck?

    @Query("SELECT p FROM ProductEntity p ORDER BY p.id")
    @Transactional(readOnly = true)
    fun streamAll(): Stream<ProductEntity>
}
