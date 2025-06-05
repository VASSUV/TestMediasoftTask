package ru.vassuv.testmediasofttask.warehouse.controller.mappers

import ru.vassuv.testmediasofttask.warehouse.controller.response.CustomerReportInfoResponse
import ru.vassuv.testmediasofttask.warehouse.controller.response.ProductOrderReportInfoResponse
import ru.vassuv.testmediasofttask.warehouse.service.model.CustomerReportInfo
import ru.vassuv.testmediasofttask.warehouse.service.model.ProductOrderReportInfo

/**
 * Мапперы для преобразования моделей, связанных с отчетами.
 */

internal fun ProductOrderReportInfo.toProductOrderReportInfoResponse() = ProductOrderReportInfoResponse(
    orderId = this.orderId,
    customer = this.customer.toProductOrderReportInfoResponse(),
    status = this.status,
    deliveryAddress = this.deliveryAddress,
    quantity = this.quantity,
)

internal fun CustomerReportInfo.toProductOrderReportInfoResponse() = CustomerReportInfoResponse(
    id = this.id,
    login = this.login,
    email = this.email,
    inn = this.inn,
    accountNumber = this.accountNumber,
)
