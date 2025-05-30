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

open class EntityManagerScheduler(
    private val sessionFactory: SessionFactory,
    private val entityManager: EntityManager,
    private val schedulingProperties: SchedulingProperties,
) {
    private val log = LoggerFactory.getLogger(EntityManagerScheduler::class.java)

    @Transactional
    @Scheduled(cron = "\${scheduling.price-change.entity-manager.cron}") // каждую минуту
    @LogExecutionTime
    open fun updateAllPricesBatchBySession() {
        log.info("EntityManagerScheduler. Начал обновлять цену")
        val entityManagerProps = schedulingProperties.priceChange.entityManager
        val percent = entityManagerProps.percent.toBigDecimal()
        val exportFilePath = entityManagerProps.exportFilePath.ifEmpty { error("Export file path parameter is empty") }
        val pageSize = entityManagerProps.batchSize
        val file = File(exportFilePath)

        file.parentFile?.run {
            if (!exists()) mkdirs()
        }

        val bufferedWriter = BufferedWriter(FileWriter(exportFilePath, true))
        bufferedWriter.use { logFile ->
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
                        // session автоматически отслеживает изменения

                        product.apply {
                            logFile.write("$id $name $price $description $article $createdAt\n")
                            count++
                        }
                    }

                    session.flush()  // выполняет SQL
                    session.clear()  // очищает кэш — чтобы объекты не росли в памяти

                    transaction.commit()
                    session.close()

                    println("Updated batch ${page + 1} (${products.size} records)")

                    page++
                    log.info("Успешно записал ${pageSize * (page - 1) + products.size} продуктов в файл $exportFilePath")
                } catch (e: Exception) {
                    transaction.rollback()
                    session.close()
                    throw e
                }
            }
            log.info("Успешно обновил все цены и записал все записи в файл")
            logFile.flush()
        }
    }

//    @Transactional
//    @Scheduled(cron = "\${scheduling.price-change.entity-manager..cron}") // каждую минуту
//    @LogExecutionTime
//    open fun updateAllPricesBatchByEntityManager() {
//        log.info("EntityManagerScheduler. Начал обновлять цену")
//        val entityManagerProps = schedulingProperties.priceChange.entityManager
//        val percent = entityManagerProps.percent.toBigDecimal()
//        val exportFilePath = entityManagerProps.exportFilePath.ifEmpty { error("Export file path parameter is empty") }
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
}
