package ru.vassuv.testmediasofttask.warehouse.exception

import java.util.UUID

class ProductNotFoundException(val id: UUID): Throwable()

class ProductIsExistWithArticleException(): Throwable()