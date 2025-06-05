package ru.vassuv.testmediasofttask.warehouse.scheduling

import jakarta.persistence.EntityManager
import org.hibernate.SessionFactory
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.annotation.Transactional
import ru.vassuv.testmediasofttask.warehouse.aop.LogExecutionTime
import ru.vassuv.testmediasofttask.warehouse.config.properties.SchedulingProperties
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.math.BigDecimal
import kotlin.use

/**
 * Шедулер, выполняющий пакетное обновление цен продуктов с использованием Hibernate [SessionFactory].
 *
 * Данные обновляются по страницам (батчами) с сохранением результатов в файл.
 *
 * @property sessionFactory Фабрика сессий Hibernate для доступа к сущностям.
 * @property entityManager Менеджер сущностей JPA для выполнения запросов (не используется в текущем методе).
 * @property schedulingProperties Параметры для управления процессом изменения цен.
 *
 * @see [SessionFactory]
 * @see [SchedulingProperties]
 * @see [ProductEntity]
 */
open class EntityManagerScheduler(
    private val sessionFactory: SessionFactory,
    private val entityManager: EntityManager,
    private val schedulingProperties: SchedulingProperties,
) {
    private val log = LoggerFactory.getLogger(EntityManagerScheduler::class.java)

    /**
     * Пакетное обновление цен всех продуктов и запись результатов в файл согласно расписанию.
     *
     * Использует постраничную обработку для снижения нагрузки на память.
     *
     * @see [Scheduled]
     * @see [Transactional]
     * @see [LogExecutionTime]
     */
    @Transactional
    @Scheduled(cron = "\${scheduling.price-change.entity-manager.cron}")
    @LogExecutionTime
    @Suppress("TooGenericExceptionCaught")
    open fun updateAllPricesBatchBySession() {
        log.info("EntityManagerScheduler: Начало обновления цен продуктов")

        val entityManagerProps = schedulingProperties.priceChange.entityManager
        val percent = entityManagerProps.percent.toBigDecimal()
        val exportFilePath = entityManagerProps.exportFilePath.ifEmpty { error("Export file path parameter is empty") }
        val pageSize = entityManagerProps.batchSize
        // TODO возможно следует выбирать batch size динамически, из размера таблицы

        val file = File(exportFilePath)
        file.parentFile?.apply { if (!exists()) mkdirs() }

        BufferedWriter(FileWriter(exportFilePath, true)).use { logFile ->
            var count = 0
            var page = 0

            while (true) {
                val session = sessionFactory.openSession()
                val transaction = session.beginTransaction()

                try {
                    val products = session.createQuery(
                        "FROM ProductEntity",
                        ProductEntity::class.java
                    )
                        .setFirstResult(page * pageSize)
                        .setMaxResults(pageSize)
                        .list()

                    if (products.isEmpty()) {
                        transaction.rollback()
                        session.close()
                        break
                    }

                    for (product in products) {
                        product.price = product.price.multiply(percent)

                        val log = product.run { "$id $name $price $description $article $createdAt\n" }
                        logFile.write(log)
                        count++
                    }

                    session.flush()
                    session.clear()

                    transaction.commit()
                    session.close()

                    log.info("Обновлена страница ${page + 1} (записей: ${products.size}), всего записей: $count")
                    page++
                } catch (e: Exception) {
                    transaction.rollback()
                    session.close()
                    log.error("Ошибка обновления цен продуктов на странице ${page + 1}: ${e.message}", e)
                    throw e
                }
            }

            log.info("Завершено обновление цен продуктов. Всего записей: $count")
            logFile.flush()
        }
    }
}

//    @Transactional
//    @Scheduled(cron = "\${scheduling.price-change.entity-manager..cron}") // каждую минуту
//    @LogExecutionTime
//    open fun updateAllPricesBatchByEntityManager() {
//        log.info("EntityManagerScheduler. Начал обновлять цену")
//        val entityManagerProps = schedulingProperties.priceChange.entityManager
//        val percent = entityManagerProps.percent.toBigDecimal()
//      val exportFilePath = entityManagerProps.exportFilePath.ifEmpty { error("Export file path parameter is empty") }
//        val pageSize = entityManagerProps.batchSize
//        val file = File(exportFilePath)
//
//        file.parentFile?.run {
//            if (!exists()) mkdirs()
//        }
//
//        val bufferedWriter = BufferedWriter(FileWriter(exportFilePath, true))
//        bufferedWriter.use { logFile ->
//
//            var offset = 0
//            var count = 0
//            var page = 0
//
//            while (true) {
//                // 1. Получаем пачку сущностей
//                val products = entityManager.createQuery(
//                    "SELECT p FROM ProductEntity p",
//                    ProductEntity::class.java
//                )
//                    .setFirstResult(offset)
//                    .setMaxResults(pageSize)
//                    .resultList
//
//                if (products.isEmpty()) break
//
//                // 2. Меняем цену
//                products.forEach {
//                    it.price = it.price + it.price * percent
//                    it.apply {
//                        logFile.write("$id $name $price $description $article $createdAt\n")
//                        count++
//                    }
//                }
//
//                // 3. Сохраняем изменения и очищаем контекст
//                entityManager.flush()
//                entityManager.clear()
//
//                offset += pageSize
//                page++
//                log.info("Успешно записал ${pageSize * (page - 1) + products.size} продуктов в файл $exportFilePath")
//            }
//            log.info("Успешно обновил все цены и записал все записи в файл")
//            logFile.flush()
//        }
//    }
//}
