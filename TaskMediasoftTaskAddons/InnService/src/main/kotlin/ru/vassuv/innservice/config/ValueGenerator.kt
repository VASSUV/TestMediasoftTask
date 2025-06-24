package ru.vassuv.innservice.config

import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class ValueGenerator {

    private val random = SecureRandom()

    fun generateDigits(length: Int): String {
        Thread.sleep(3000)
        return buildString {
            repeat(length) {
                append(random.nextInt(10)) // цифра от 0 до 9
            }
        }
    }

    fun generateInn(): String = generateDigits(12)
}