package ru.vassuv.testmediasofttask.warehouse.persist.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import ru.vassuv.testmediasofttask.warehouse.persist.entity.projecttion.ProductWithConflictCheckProjection
import java.util.*
import java.util.stream.Stream

/**
 * Репозиторий для выполнения CRUD-операций и запросов к товарам ([ProductEntity]).
 *
 * Использует стандартные возможности Spring Data JPA, а также кастомные запросы.
 */
interface ProductRepository : JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {

    /**
     * Проверяет существование товара по уникальному артикулу.
     *
     * @param article артикул товара.
     * @return true, если товар с указанным артикулом существует; иначе false.
     */
    fun existsProductEntityByArticle(article: String): Boolean

    /**
     * Ищет товар по идентификатору и проверяет конфликт по артикулу.
     *
     * @param id идентификатор проверяемого товара.
     * @param article артикул для проверки конфликта.
     * @return проекция с данными о товаре и флагом конфликта ([ProductWithConflictCheckProjection]).
     */
    @Query(
        """
        SELECT p as product, 
               EXISTS(
                   SELECT 1 FROM ProductEntity p2 
                   WHERE p2.article = :article AND p2.id <> :id
               ) AS articleExists
        FROM ProductEntity p
        WHERE p.id = :id
    """
    )
    fun findByIdWithArticleConflict(id: UUID, article: String): ProductWithConflictCheckProjection?

    /**
     * Получает все товары в виде стрима.
     *
     * Используется для больших объёмов данных, например, при экспорте.
     *
     * @return поток сущностей товаров ([ProductEntity]).
     */
    @Query("SELECT p FROM ProductEntity p ORDER BY p.id")
    @Transactional(readOnly = true)
    fun streamAll(): Stream<ProductEntity>
}