package ru.vassuv.testmediasofttask.warehouse.controller.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import jakarta.persistence.criteria.Expression
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.Article
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.Category
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.CreatedAt
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.Description
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.FieldNames
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.Id
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.Name
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.Price
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.Quantity
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.QuantityUpdatedAt
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterRequest.AndFilter
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterRequest.EqualFilter
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterRequest.GreaterOrEqualFilter
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterRequest.GreaterThanFilter
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterRequest.LessOrEqualFilter
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterRequest.LessThanFilter
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterRequest.LikeFilter
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterRequest.NotEqualFilter
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterRequest.OrFilter
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = AndFilter::class, name = "AND"),
    JsonSubTypes.Type(value = AndFilter::class, name = "&&"),
    JsonSubTypes.Type(value = OrFilter::class, name = "OR"),
    JsonSubTypes.Type(value = OrFilter::class, name = "||"),
    JsonSubTypes.Type(value = EqualFilter::class, name = "EQUAL"),
    JsonSubTypes.Type(value = EqualFilter::class, name = "="),
    JsonSubTypes.Type(value = NotEqualFilter::class, name = "NOT_EQUAL"),
    JsonSubTypes.Type(value = NotEqualFilter::class, name = "<>"),
    JsonSubTypes.Type(value = LikeFilter::class, name = "LIKE"),
    JsonSubTypes.Type(value = LikeFilter::class, name = "~"),
    JsonSubTypes.Type(value = GreaterThanFilter::class, name = "GREATER_THAN"),
    JsonSubTypes.Type(value = GreaterThanFilter::class, name = ">"),
    JsonSubTypes.Type(value = LessThanFilter::class, name = "LESS_THAN"),
    JsonSubTypes.Type(value = LessThanFilter::class, name = "<"),
    JsonSubTypes.Type(value = GreaterOrEqualFilter::class, name = "GREATER_OR_EQUAL"),
    JsonSubTypes.Type(value = GreaterOrEqualFilter::class, name = ">="),
    JsonSubTypes.Type(value = LessOrEqualFilter::class, name = "LESS_OR_EQUAL"),
    JsonSubTypes.Type(value = LessOrEqualFilter::class, name = "<="),
)
interface SearchProductFilterRequest {
    fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate

    data class AndFilter(val filters: List<SearchProductFilterRequest>) : SearchProductFilterRequest {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate =
            cb.and(*filters.map { it.toPredicate(root, cb) }.toTypedArray())
    }

    data class OrFilter(val filters: List<SearchProductFilterRequest>) : SearchProductFilterRequest {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate =
            cb.or(*filters.map { it.toPredicate(root, cb) }.toTypedArray())
    }

    data class NotEqualFilter(val field: SearchProductFilterField<Any>) : SearchProductFilterRequest {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder) =
            cb.notEqual(field.getExpression(root), field.value)
    }

    data class EqualFilter(val field: SearchProductFilterField<Any>) : SearchProductFilterRequest {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder) =
            cb.equal(field.getExpression(root), field.value)
    }

    data class LikeFilter(val field: SearchProductFilterField<String>) : SearchProductFilterRequest {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate =
            cb.like(cb.lower(field.getExpression(root)), "%${field.value.lowercase()}%")
    }

    data class GreaterThanFilter(val field: SearchProductFilterField<Comparable<Any>>) : SearchProductFilterRequest {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate =
            cb.greaterThan(field.getExpression(root), field.value)
    }

    data class GreaterOrEqualFilter(val field: SearchProductFilterField<Comparable<Any>>) : SearchProductFilterRequest {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate =
            cb.greaterThanOrEqualTo(field.getExpression(root), field.value)
    }

    data class LessThanFilter(val field: SearchProductFilterField<Comparable<Any>>) : SearchProductFilterRequest {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate =
            cb.lessThan(field.getExpression(root), field.value)
    }

    data class LessOrEqualFilter(val field: SearchProductFilterField<Comparable<Any>>) : SearchProductFilterRequest {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate =
            cb.lessThanOrEqualTo(field.getExpression(root), field.value)
    }
}


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name")
@JsonSubTypes(
    JsonSubTypes.Type(value = Id::class, name = FieldNames.id),
    JsonSubTypes.Type(value = Name::class, name = FieldNames.name),
    JsonSubTypes.Type(value = Description::class, name = FieldNames.description),
    JsonSubTypes.Type(value = Article::class, name = FieldNames.article),
    JsonSubTypes.Type(value = Category::class, name = FieldNames.category),
    JsonSubTypes.Type(value = Price::class, name = FieldNames.price),
    JsonSubTypes.Type(value = Quantity::class, name = FieldNames.quantity),
    JsonSubTypes.Type(value = CreatedAt::class, name = FieldNames.createdAt),
    JsonSubTypes.Type(value = QuantityUpdatedAt::class, name = FieldNames.quantityUpdatedAt),
)
sealed interface SearchProductFilterField<T> {
    val value: T

    fun getExpression(root: Root<*>): Expression<T>

    class Id(override val value: UUID): SearchProductFilterField<UUID> {
        override fun getExpression(root: Root<*>) = root.get<UUID>(FieldNames.id)!!
    }
    class Name(override val value: String): SearchProductFilterField<String> {
        override fun getExpression(root: Root<*>) = root.get<String>(FieldNames.name)!!
    }
    class Description(override val value: String): SearchProductFilterField<String> {
        override fun getExpression(root: Root<*>) = root.get<String>(FieldNames.description)!!
    }
    class Article(override val value: String): SearchProductFilterField<String> {
        override fun getExpression(root: Root<*>) = root.get<String>(FieldNames.article)!!
    }
    class Category(override val value: String): SearchProductFilterField<String> {
        override fun getExpression(root: Root<*>) = root.get<String>(FieldNames.category)!!
    }
    class Price(override val value: BigDecimal): SearchProductFilterField<BigDecimal> {
        override fun getExpression(root: Root<*>) = root.get<BigDecimal>(FieldNames.price)!!
    }
    class Quantity(override val value: BigDecimal): SearchProductFilterField<BigDecimal> {
        override fun getExpression(root: Root<*>) = root.get<BigDecimal>(FieldNames.quantity)!!
    }
    class CreatedAt(
        @JsonFormat(pattern = "dd-MM-yyyy")
        override val value: LocalDate
    ): SearchProductFilterField<LocalDate> {
        override fun getExpression(root: Root<*>) = root.get<LocalDate>(FieldNames.createdAt)!!
    }
    class QuantityUpdatedAt(override val value: ZonedDateTime): SearchProductFilterField<ZonedDateTime> {
        override fun getExpression(root: Root<*>) = root.get<ZonedDateTime>(FieldNames.quantity)!!
    }

    object FieldNames {
        const val id = "id"
        const val name = "name"
        const val description = "description"
        const val article = "article"
        const val category = "category"
        const val price = "price"
        const val quantity = "quantity"
        const val createdAt = "createdAt"
        const val quantityUpdatedAt = "quantityUpdatedAt"
    }
}