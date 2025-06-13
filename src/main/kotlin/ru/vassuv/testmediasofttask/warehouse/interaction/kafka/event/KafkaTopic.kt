package ru.vassuv.testmediasofttask.warehouse.interaction.kafka.event

/**
 * Наименования топиков для kafka
 *
 * @property topicName наименование
 */
enum class KafkaTopic(val topicName: String) {
    WAREHOUSE("warehouse_topic"),
    DELETE_PRODUCT_IMAGE("delete_product_image_topic"),
    TEST("test_topic"),
}
