package ru.vassuv.testmediasofttask.warehouse.persist.repository

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ProductRepositoryTest : AbstractRepositoryTest() {

    @Test
    fun `should find product by article`() {
        val exists = productRepository.existsProductEntityByArticle("ART_2")
        assertThat(exists).isTrue()
    }

    @Test
    fun `should stream all products`() {
        productRepository.streamAll().use { stream ->
            val result = stream.toList()
            assertThat(result).isNotEmpty
        }
    }
}
