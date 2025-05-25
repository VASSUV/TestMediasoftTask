package ru.vassuv.testmediasofttask.warehouse.utils

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

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
        val methodSignature = joinPoint.signature.toShortString()
        val startTime = System.currentTimeMillis()

        val result = joinPoint.proceed()

        val executionTime = System.currentTimeMillis() - startTime
        log.info("Transactional Method $methodSignature executed for $executionTime ms")

        return result
    }
}