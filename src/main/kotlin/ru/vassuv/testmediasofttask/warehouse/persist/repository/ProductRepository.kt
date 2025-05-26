package ru.vassuv.testmediasofttask.warehouse.persist.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import java.util.UUID
import java.util.stream.Stream

/**
 * Репозиторий для выполнения операций над товарами в базе данных,
 * где за счет интерфеса JPARepository добавляются CRUD операции к БД
 */
interface ProductRepository : JpaRepository<ProductEntity, UUID> {
    fun findByArticle(article: String): ProductEntity?

    @Query(
        "SELECT * FROM products ORDER BY products.id",
        nativeQuery = true
    )
    @Transactional(readOnly = true)
    fun streamAll(): Stream<ProductEntity>
}
