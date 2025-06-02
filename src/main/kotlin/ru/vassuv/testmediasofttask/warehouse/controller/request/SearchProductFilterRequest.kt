package ru.vassuv.testmediasofttask.warehouse.controller.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.Article
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.Category
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.CreatedAt
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.Description
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.Id
import ru.vassuv.testmediasofttask.warehouse.controller.request.SearchProductFilterField.IsAvailable
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
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID

typealias Criteria = SearchProductFilterRequest
typealias AnyField = SearchProductFilterField<Any>
typealias UuidField = SearchProductFilterField<UUID>
typealias StringField = SearchProductFilterField<String>
typealias BigDecimalField = SearchProductFilterField<BigDecimal>
typealias LocalDateField = SearchProductFilterField<LocalDate>
typealias ZonedDateTimeField = SearchProductFilterField<ZonedDateTime>
typealias ComparableField = SearchProductFilterField<Comparable<Any>>
typealias BooleanField = SearchProductFilterField<Boolean>

/**
 * Интерфейс, определяющий структуру фильтров для сложного поиска товаров.
 * Использует шаблон "делегирования" (`Delegate`) для упрощения генерации предикатов.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(AndFilter::class, name = "AND"),
    JsonSubTypes.Type(AndFilter::class, name = "&&"),
    JsonSubTypes.Type(OrFilter::class, name = "OR"),
    JsonSubTypes.Type(OrFilter::class, name = "||"),
    JsonSubTypes.Type(EqualFilter::class, name = "EQUAL"),
    JsonSubTypes.Type(EqualFilter::class, name = "="),
    JsonSubTypes.Type(NotEqualFilter::class, name = "NOT_EQUAL"),
    JsonSubTypes.Type(NotEqualFilter::class, name = "<>"),
    JsonSubTypes.Type(LikeFilter::class, name = "LIKE"),
    JsonSubTypes.Type(LikeFilter::class, name = "~"),
    JsonSubTypes.Type(GreaterThanFilter::class, name = "GREATER_THAN"),
    JsonSubTypes.Type(GreaterThanFilter::class, name = ">"),
    JsonSubTypes.Type(LessThanFilter::class, name = "LESS_THAN"),
    JsonSubTypes.Type(LessThanFilter::class, name = "<"),
    JsonSubTypes.Type(GreaterOrEqualFilter::class, name = "GREATER_OR_EQUAL"),
    JsonSubTypes.Type(GreaterOrEqualFilter::class, name = ">="),
    JsonSubTypes.Type(LessOrEqualFilter::class, name = "LESS_OR_EQUAL"),
    JsonSubTypes.Type(LessOrEqualFilter::class, name = "<="),
)
interface SearchProductFilterRequest {

    /**
     * Генерирует [Predicate] из текущего фильтра.
     *
     * @param root Корень запроса (Criteria API).
     * @param cb Построитель критериев запросов.
     */
    fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate

    /** Логическое объединение условий с оператором AND. */
    data class AndFilter(val filters: List<Criteria>) : Criteria {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate =
            cb.and(*filters.map { it.toPredicate(root, cb) }.toTypedArray())
    }

    /** Логическое объединение условий с оператором OR. */
    data class OrFilter(val filters: List<Criteria>) : Criteria {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate =
            cb.or(*filters.map { it.toPredicate(root, cb) }.toTypedArray())
    }

    /** Условие на неравенство. */
    data class NotEqualFilter(val field: AnyField) : Criteria {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder) =
            cb.notEqual(field.getExpression(root), field.value)!!
    }

    /** Условие на равенство. */
    data class EqualFilter(val field: AnyField) : Criteria {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder) =
            cb.equal(field.getExpression(root), field.value)!!
    }

    /** Условие на частичное совпадение строки (LIKE). */
    data class LikeFilter(val field: StringField) : Criteria {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate =
            cb.like(cb.lower(field.getExpression(root)), "%${field.value.lowercase()}%")
    }

    /** Условие, проверяющее, что значение поля больше заданного. */
    data class GreaterThanFilter(val field: ComparableField) : Criteria {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate =
            cb.greaterThan(field.getExpression(root), field.value)
    }

    /** Условие, проверяющее, что значение поля больше или равно заданному. */
    data class GreaterOrEqualFilter(val field: ComparableField) : Criteria {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate =
            cb.greaterThanOrEqualTo(field.getExpression(root), field.value)
    }

    /** Условие, проверяющее, что значение поля меньше заданного. */
    data class LessThanFilter(val field: ComparableField) : Criteria {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate =
            cb.lessThan(field.getExpression(root), field.value)
    }

    /** Условие, проверяющее, что значение поля меньше или равно заданному. */
    data class LessOrEqualFilter(val field: ComparableField) : Criteria {
        override fun toPredicate(root: Root<*>, cb: CriteriaBuilder): Predicate =
            cb.lessThanOrEqualTo(field.getExpression(root), field.value)
    }
}

/**
 * Классы-поля фильтрации для многокритериального поиска товаров.
 * Каждый класс соответствует конкретному полю сущности [ProductEntity].
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name")
sealed class SearchProductFilterField<T>(
    /** Имя поля сущности для фильтрации, устанавливается при десериализации JSON. */
    val name: String
) {

    /** Значение поля фильтрации, используемое для проверки условия. */
    abstract val value: T

    /**
     * Получение [Expression] для поля из корневого элемента запроса.
     *
     * @param root Корень запроса Criteria API.
     */
    fun getExpression(root: Root<*>) = root.get<T>(name)!!

    /** Уникальный идентификатор товара. */
    @JsonTypeName("id")
    class Id(override val value: UUID) : UuidField("id")

    /** Название товара. */
    @JsonTypeName("name")
    class Name(override val value: String) : StringField("name")

    /** Описание товара. */
    @JsonTypeName("description")
    class Description(override val value: String) : StringField("description")

    /** Артикул товара. */
    @JsonTypeName("article")
    class Article(override val value: String) : StringField("article")

    /** Категория товара. */
    @JsonTypeName("category")
    class Category(override val value: String) : StringField("category")

    /** Цена товара. */
    @JsonTypeName("price")
    class Price(override val value: BigDecimal) : BigDecimalField("price")

    /** Количество товара на складе. */
    @JsonTypeName("quantity")
    class Quantity(override val value: BigDecimal) : BigDecimalField("quantity")

    /** Дата создания товара. */
    @JsonTypeName("createdAt")
    class CreatedAt(
        @JsonFormat(pattern = "dd-MM-yyyy")
        override val value: LocalDate
    ) : LocalDateField("created_at")

    /** Дата последнего обновления количества товара. */
    @JsonTypeName("quantityUpdatedAt")
    class QuantityUpdatedAt(override val value: ZonedDateTime) : ZonedDateTimeField("quantity_updated_at")

    /** Флаг доступности товара. */
    @JsonTypeName("isAvailable")
    class IsAvailable(override val value: Boolean) : BooleanField("is_available")
}