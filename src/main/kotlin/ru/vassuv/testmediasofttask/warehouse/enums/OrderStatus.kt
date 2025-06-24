package ru.vassuv.testmediasofttask.warehouse.enums

/**
 * Статусы, возможные для заказов.
 */
enum class OrderStatus {
    /** Созданный заказ */
    CREATED,

    /** Подтверждённый заказ */
    CONFIRMED,

    /** Отменённый заказ */
    CANCELED,

    /** Заказ в обработке */
    PROCESSING,

    /** Выполненный заказ */
    DONE,

    /** Отклонённый заказ */
    REJECTED
}
