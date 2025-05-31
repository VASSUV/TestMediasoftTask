package ru.vassuv.testmediasofttask.warehouse.persist.entity.projecttion

import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductEntity

interface ProductWithConflictCheckProjection {
    val product: ProductEntity?
    val articleExists: Boolean
}