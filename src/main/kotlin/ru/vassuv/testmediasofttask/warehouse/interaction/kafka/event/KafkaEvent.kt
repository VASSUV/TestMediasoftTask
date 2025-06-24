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
    JsonSubTypes.Type(KafkaEvent.Order.UpdateStatus::class, name = "UPDATE_ORDER_STATUS"),
    JsonSubTypes.Type(KafkaEvent.Product.DeleteImage::class, name = "DELETE_PRODUCT_IMAGE"),
    JsonSubTypes.Type(KafkaEvent.Compliance.CheckOrderRequest::class, name = "CHECK_COMPLIANCE_ORDER_REQUEST"),
)
sealed interface KafkaEvent {

    /**
     * Интерфейс для обобщения событий по типу Product
     */
    sealed interface Product : KafkaEvent {

        /**
         * Модель события удаления файлов изображений для продукта
         *
         * @property productId id продукта
         * @property keys ключи для доступа к файлам
         */
        data class DeleteImage(
            val productId: UUID,
            val keys: List<String>
        ): Product
    }

    /**
     * Интерфейс для обобщения событий по типу Compliance
     */
    sealed interface Compliance : KafkaEvent {

        /**
         * Модель события с запроса на проверки Compliance
         *
         * @property login логин Заказчика
         * @property inn инн заказчика
         * @property businessKey бизнес ключ процесса заказа
         */
        data class CheckOrderRequest(
            val login: String,
            val inn: String,
            val businessKey: String
        ): Product
    }

    /**
     * Интерфейс для обобщения событий по типу Заказ
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

