package ru.vassuv.testmediasofttask.warehouse.camunda

import org.springframework.stereotype.Component
import ru.vassuv.testmediasofttask.warehouse.enums.OrderStatus
import ru.vassuv.testmediasofttask.warehouse.service.OrderService

@Component("setStatusConfirmedDelegate")
class SetStatusConfirmedDelegate(orderService: OrderService) :
    SetStatusAbstractDelegate(orderService, OrderStatus.CONFIRMED)
