package ru.vassuv.testmediasofttask.warehouse.persist.entity

interface ProductWithConflictCheck {
    val product: ProductEntity?
    val articleExists: Boolean
}
