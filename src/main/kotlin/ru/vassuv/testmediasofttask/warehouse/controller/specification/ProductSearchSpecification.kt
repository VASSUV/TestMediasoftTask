package ru.vassuv.testmediasofttask.warehouse.controller.specification

import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import ru.vassuv.testmediasofttask.warehouse.controller.params.ProductSearchParams
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterRequest
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import java.util.UUID

/**
 * Спецификации для выполнения поиска продуктов с использованием JPA Criteria API.
 */
object ProductSearchSpecification {

    /**
     * Создаёт спецификацию поиска продуктов по простым критериям.
     *
     * @param criteria Параметры поиска продуктов.
     * @return Спецификация для поиска продуктов.
     *
     * @see ProductSearchParams
     * @see Specification
     */
    @Suppress("SpreadOperator")
    fun fromCriteria(criteria: ProductSearchParams): Specification<ProductEntity> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            criteria.ids?.takeIf { it.isNotEmpty() }?.let {
                predicates += root.get<UUID>("id").`in`(it)
            }

            criteria.name?.let {
                predicates += cb.like(cb.lower(root.get("name")), "%${it.lowercase()}%")
            }

            criteria.article?.let {
                predicates += cb.like(cb.lower(root.get("article")), "%${it.lowercase()}%")
            }

            criteria.description?.let {
                predicates += cb.like(cb.lower(root.get("description")), "%${it.lowercase()}%")
            }

            criteria.category?.let {
                predicates += cb.equal(root.get<String>("category"), it)
            }

            criteria.minPrice?.let {
                predicates += cb.greaterThanOrEqualTo(root.get("price"), it)
            }

            criteria.maxPrice?.let {
                predicates += cb.lessThanOrEqualTo(root.get("price"), it)
            }

            criteria.minQuantity?.let {
                predicates += cb.greaterThanOrEqualTo(root.get("quantity"), it)
            }

            criteria.maxQuantity?.let {
                predicates += cb.lessThanOrEqualTo(root.get("quantity"), it)
            }

            criteria.createdAfter?.let {
                predicates += cb.greaterThanOrEqualTo(root.get("createdAt"), it)
            }

            criteria.createdBefore?.let {
                predicates += cb.lessThanOrEqualTo(root.get("createdAt"), it)
            }

            criteria.updatedAfter?.let {
                predicates += cb.greaterThanOrEqualTo(root.get("quantityUpdatedAt"), it)
            }

            criteria.updatedBefore?.let {
                predicates += cb.lessThanOrEqualTo(root.get("quantityUpdatedAt"), it)
            }

            cb.and(*predicates.toTypedArray())
        }
    }

    /**
     * Создаёт спецификацию поиска продуктов на основе сложных (вложенных) критериев поиска.
     *
     * @param filter Сложный фильтр поиска, который преобразуется в Predicate.
     * @return Спецификация для поиска продуктов.
     *
     * @see SearchProductFilterRequest
     * @see Specification
     */
    fun fromSmartCriteria(filter: SearchProductFilterRequest): Specification<ProductEntity> {
        return Specification { root, _, cb -> filter.toPredicate(root, cb) }
    }
}
