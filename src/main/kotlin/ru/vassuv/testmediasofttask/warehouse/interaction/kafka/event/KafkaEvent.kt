package ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus
import java.math.BigDecimal
import java.util.UUID

/**
 * Интерфейс с дискриминатором для сериализации разных событий для kafka
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "event")
@JsonSubTypes(
    JsonSubTypes.Type(KafkaEvent.Order.Create::class, name = "CREATE_ORDER"),
    JsonSubTypes.Type(KafkaEvent.Order.Update::class, name = "UPDATE_ORDER"),
    JsonSubTypes.Type(KafkaEvent.Order.Delete::class, name = "DELETE_ORDER"),
    JsonSubTypes.Type(KafkaEvent.Order.UpdateStatus::class, name = "UPDATE_ORDER STATUS"),
)
sealed interface KafkaEvent {

    sealed interface Product : KafkaEvent {

        data class DeleteImage(
            val productId: UUID,
            val keys: List<String>
        ): Product
    }

    /**
     * Интерфейс для обощения событий по типу Заказ
     */
    sealed interface Order : KafkaEvent {

        /**
         * Модель события с Созданием заказа
         *
         * @property customerId id заказчика
         * @property deliveryAddress адрес доставки
         * @property products список продуктов в заказе [Product]
         */
        data class Create(
            val customerId: UUID,
            val deliveryAddress: String,
            val products: List<Product>
        ) : Order {

            /**
             * Модель продукта для события Создания заказа
             *
             * @property id id продукта
             * @property quantity количество заказанного продукта
             */
            data class Product(val id: UUID, val quantity: BigDecimal)
        }

        /**
         * Модель события с Обновлением заказа
         *
         * @property orderId id заказа
         * @property customerId id заказчика
         * @property products список продуктов в заказе [Product]
         */
        data class Update(
            val orderId: UUID,
            val customerId: UUID,
            val products: List<Product>
        ) : Order {

            /**
             * Модель продукта для события Создания заказа
             *
             * @property id id продукта
             * @property quantity количество заказанного продукта
             */
            data class Product(val id: UUID, val quantity: BigDecimal)
        }

        /**
         * Модель события с Удалением заказа
         *
         * @property orderId id заказа
         * @property customerId id заказчика
         */
        data class Delete(val orderId: UUID, val customerId: UUID) : Order

        /**
         * Модель события с Обновлением статуса заказа
         *
         * @property orderId id заказа
         * @property status статус заказа [OrderStatus]
         */
        data class UpdateStatus(val orderId: UUID, val status: OrderStatus) : Order
    }
}

