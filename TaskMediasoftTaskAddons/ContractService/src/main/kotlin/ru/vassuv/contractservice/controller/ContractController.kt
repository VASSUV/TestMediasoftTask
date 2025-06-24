package ru.vassuv.contractservice.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.vassuv.contractservice.controller.model.RegisterContractRequest
import ru.vassuv.contractservice.controller.model.RegisterContractResponse
import ru.vassuv.contractservice.service.ContractService
import java.util.UUID

@RestController
@RequestMapping("/api/contract")
class ContractController(
    private val contractService: ContractService
) {
    @PostMapping
    fun register(@RequestBody req: RegisterContractRequest): ResponseEntity<RegisterContractResponse> {
        val id = contractService.register(req.inn, req.accountNumber)
        return ResponseEntity.ok(RegisterContractResponse(id))
    }

    @DeleteMapping("/enable/{contractId}")
    fun cancel(@PathVariable contractId: UUID): ResponseEntity<Void> {
        contractService.cancelContract(contractId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/enable/{contractId}")
    fun enable(@PathVariable contractId: UUID): ResponseEntity<Void> {
        contractService.enableContract(contractId)
        return ResponseEntity.ok().build()
    }
}
