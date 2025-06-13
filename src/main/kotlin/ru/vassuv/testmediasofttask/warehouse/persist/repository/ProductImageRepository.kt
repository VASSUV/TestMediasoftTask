package ru.vassuv.testmediasofttask.warehouse.persist.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.vassuv.testmediasofttask.warehouse.persist.entity.ProductImageEntity
import java.util.UUID

interface ProductImageRepository : JpaRepository<ProductImageEntity, UUID> {
    fun findAllByProductId(productId: UUID): List<ProductImageEntity>
    fun deleteAllByProductId(productId: UUID)
}
