package ru.vassuv.testmediasofttask.warehouse.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.vassuv.testmediasofttask.warehouse.support.Consts.SecurityRoleRules.ROLES_NOT_USER
import ru.vassuv.testmediasofttask.warehouse.controller.mappers.toCreatedCustomer
import ru.vassuv.testmediasofttask.warehouse.controller.request.CreateCustomerRequest
import ru.vassuv.testmediasofttask.warehouse.controller.response.CustomerCreatedResponse
import ru.vassuv.testmediasofttask.warehouse.service.CustomerService

/**
 * Реализация REST-контроллера для управления заказчиками.
 *
 * Этот контроллер отвечает за обработку HTTP-запросов,
 * связанных с созданием и управлением заказчиками в системе.
 */
@RestController
@RequestMapping("/api/customer")
class CustomerControllerImpl(
    /**
     * Сервис для выполнения бизнес-логики, связанной с заказчиками.
     */
    private val customerService: CustomerService
) : CustomerController {

    /**
     * Обрабатывает POST-запрос на создание нового заказчика.
     *
     * Проводит валидацию входящих данных и использует сервисный слой для сохранения заказчика.
     *
     * @param request объект с данными нового заказчика, должен быть валиден.
     * @return HTTP-ответ со статусом CREATED и идентификатором нового заказчика.
     *
     * @throws CustomerAlreadyExistsException если заказчик с такими же данными уже существует.
     * @throws ValidationException если входные данные не соответствуют заданным ограничениям.
     */
    @PreAuthorize(ROLES_NOT_USER)
    @PostMapping
    override fun createCustomer(
        @Valid @RequestBody request: CreateCustomerRequest
    ): ResponseEntity<CustomerCreatedResponse> {
        val customerId = customerService.createCustomer(request.toCreatedCustomer())
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CustomerCreatedResponse(customerId))
    }
}
