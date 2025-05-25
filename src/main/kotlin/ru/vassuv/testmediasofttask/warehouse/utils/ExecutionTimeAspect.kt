package ru.vassuv.testmediasofttask.warehouse.utils

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Обработчик функции с кастомной аннотацией LogExecutionTime
 * Логирует время выполнения функции помеченной аннотацией LogExecutionTime
 */
@Aspect
@Component
class ExecutionTimeAspect {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Around("@annotation(LogExecutionTime)")
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val startTime = System.currentTimeMillis()

        val result = joinPoint.proceed() // выполнение метода

        val executionTime = System.currentTimeMillis() - startTime
        log.info("Method ${joinPoint.signature} executed for $executionTime ms")

        return result
    }
}

