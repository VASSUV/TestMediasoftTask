
package ru.vassuv.innservice.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.vassuv.innservice.service.InnService

@RestController
@RequestMapping("/api/inns")
class CustomerInnController(
    private val innService: InnService
) {
    @PostMapping("")
    fun getInns(@RequestBody logins: List<String>): Map<String, String> =
        innService.getInns(logins)
}