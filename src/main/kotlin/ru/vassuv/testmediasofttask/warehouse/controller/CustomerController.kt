package ru.vassuv.testmediasofttask.warehouse.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import ru.vassuv.testmediasofttask.warehouse.controller.request.CreateCustomerRequest
import ru.vassuv.testmediasofttask.warehouse.controller.response.CustomerCreatedResponse

/**
 * Интерфейс контроллера для работы с заказчиками.
 *
 * Предоставляет API-методы для создания новых заказчиков
 * и дальнейшего управления ими.
 */
@SecurityRequirement(name = "BearerAuth")
@Tag(
    name = "Customers",
    description = "Управление заказчиками"
)
interface CustomerController {

    /**
     * Создаёт нового заказчика и сохраняет его в базе данных.
     *
     * Выполняются проверки на уникальность заказчика по логину и email.
     *
     * @param request объект, содержащий информацию для создания нового заказчика:
     *  - login: уникальный логин заказчика.
     *  - email: email заказчика, должен быть уникальным.
     * @return ResponseEntity с объектом CustomerCreatedResponse, содержащим идентификатор созданного заказчика.
     *
     * @throws CustomerAlreadyExistsException если заказчик с указанным логином или email уже существует.
     * @throws ValidationException если входные данные не соответствуют заданным ограничениям.
     */
    @Operation(
        summary = "Создание заказчика",
        description = "Создаёт нового заказчика и добавляет запись в базу данных."
    )
    fun createCustomer(request: CreateCustomerRequest): ResponseEntity<CustomerCreatedResponse>
}
