package ru.vassuv.testmediasofttask.warehouse.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

/**
 * Аспект для логирования времени выполнения транзакционных методов, помеченных аннотацией [Transactional].
 *
 * Выполняет логирование после завершения транзакции.
 *
 * @see [org.springframework.transaction.annotation.Transactional]
 */
@Aspect
@Component
class TransactionExecutionTimeAspect {

    private val log = LoggerFactory.getLogger(this.javaClass)

    /**
     * Перехватывает выполнение транзакционных методов и логирует продолжительность их выполнения.
     *
     * Если транзакция активна, логирует после её завершения, иначе сразу после выполнения метода.
     *
     * @param joinPoint точка соединения, представляющая вызов метода.
     * @return результат выполнения метода.
     */
    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    fun logTransactionalExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val startTime = System.currentTimeMillis()
        val signature = joinPoint.signature

        log.info("STARTED: Транзакционный метод $signature")

        val result = joinPoint.proceed()

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
                override fun afterCompletion(status: Int) {
                    val executionTime = System.currentTimeMillis() - startTime
                    log.info("FINISHED: Транзакционный метод ${signature.name} выполнен за $executionTime мс")
                }
            })
        } else {
            val executionTime = System.currentTimeMillis() - startTime
            log.info("FINISHED: Нетранзакционный метод ${signature.name} выполнен за $executionTime мс")
        }

        return result
    }
}
