package ru.vassuv.testmediasofttask.warehouse.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

/**
 * Обработчик функции с аннотацией @Transactional
 * Логирует время выполнения функции помеченной аннотацией @Transactional
 */
@Aspect
@Component
class TransactionExecutionTimeAspect {

    private val log = LoggerFactory.getLogger(this.javaClass)

    // перехватываем все методы с аннотацией @Transactional
    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    fun logTransactionalExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val startTime = System.currentTimeMillis()
        val signature = joinPoint.signature
        log.info("STARTED: Transactional Method $signature")
        val result = joinPoint.proceed()

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
                override fun afterCompletion(status: Int) {
                    val executionTime = System.currentTimeMillis() - startTime
                    log.info("FINISHED: Transactional Method ${signature.name} executed for $executionTime ms")
                }
            })
        } else {
            val executionTime = System.currentTimeMillis() - startTime
            log.info("FINISHED: Non-Transactional Method ${signature.name} executed for $executionTime ms")
        }

        return result
    }
}
