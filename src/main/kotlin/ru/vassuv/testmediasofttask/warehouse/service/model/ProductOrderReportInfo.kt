package ru.vassuv.testmediasofttask.warehouse.service.model

import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus
import java.math.BigDecimal
import java.util.UUID

/**
 * Доменная модель для отчета по продуктам в заказах.
 *
 * @property orderId id заказа.
 * @property customer Информация о заказчике.
 * @property status Статус заказа.
 * @property deliveryAddress Адрес доставки.
 * @property quantity Количество товаров в заказе.
 */
data class ProductOrderReportInfo (
    val orderId: UUID,
    val customer: CustomerReportInfo,
    val status: OrderStatus,
    val deliveryAddress: String,
    val quantity: BigDecimal
)

/**
 * Доменная модель для создания нового заказа.
 *
 * @property id id заказчика.
 * @property login Логин заказчика.
 * @property email Email заказчика.
 * @property inn Инн заказчика.
 * @property accountNumber Номер счета заказчика.
 */
data class CustomerReportInfo(
    val id: UUID,
    val login: String,
    val email: String,
    val inn: String,
    val accountNumber: String
)
