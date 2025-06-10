package ru.vassuv.testmediasofttask.warehouse.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Для дебага параметров в токене
 */
@RestController
@RequestMapping("/debug")
class DebugController {

    /**
     * Для дебага параметров в токене
     *
     * @return параметры из токена
     */
    @GetMapping
    fun debug(): Any {
        val auth = SecurityContextHolder.getContext().authentication
        return mapOf(
            "principal" to auth.name,
            "authorities" to auth.authorities.map { it.authority },
            "details" to auth.details
        )
    }
}

