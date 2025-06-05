package ru.vassuv.testmediasofttask.warehouse.aop

/**
 * Аннотация для методов, время выполнения которых требуется логировать.
 *
 * Методы, помеченные данной аннотацией, логируются [ExecutionTimeAspect].
 *
 * @see [ExecutionTimeAspect]
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LogExecutionTime
