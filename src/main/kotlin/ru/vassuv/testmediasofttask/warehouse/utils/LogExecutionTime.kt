package ru.vassuv.testmediasofttask.warehouse.utils

/**
 * Аннотация для логирования времени выполнения функции, которая помечена этой аннотацией
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LogExecutionTime