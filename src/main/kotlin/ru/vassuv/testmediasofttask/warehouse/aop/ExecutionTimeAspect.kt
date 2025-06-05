package ru.vassuv.testmediasofttask.warehouse.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


/**
 * Аспект для логирования времени выполнения методов, помеченных аннотацией [LogExecutionTime].
 *
 * Использует AOP для перехвата вызовов методов и замера их продолжительности.
 *
 * @see [LogExecutionTime]
 */
@Aspect
@Component
class ExecutionTimeAspect {

    private val log = LoggerFactory.getLogger(this.javaClass)

    /**
     * Логирует время выполнения метода.
     *
     * @param joinPoint точка соединения, представляющая вызов метода.
     * @return результат выполнения метода.
     */
    @Around("@annotation(LogExecutionTime)")
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val startTime = System.currentTimeMillis()
        log.info("STARTED: Метод ${joinPoint.signature}")

        val result = joinPoint.proceed()

        val executionTime = System.currentTimeMillis() - startTime
        log.info("FINISHED: Метод ${joinPoint.signature} выполнен за $executionTime мс")

        return result
    }
}
