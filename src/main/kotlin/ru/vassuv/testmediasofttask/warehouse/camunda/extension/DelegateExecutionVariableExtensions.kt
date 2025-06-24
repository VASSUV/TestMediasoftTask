package ru.vassuv.testmediasofttask.warehouse.camunda.extension

import camundajar.impl.scala.math.BigDecimal
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.joda.time.LocalDate
import java.util.UUID

const val ORDER_ID = "orderId"
const val ADDRESS = "address"
const val LOGIN = "login"
const val INN = "inn"
const val ACCOUNT_NUMBER = "accountNumber"
const val AMOUNT = "amount"
const val CONTRACT_ID = "contractId"
const val DELIVERY_ID = "deliveryId"
const val EXPECTED_DATE = "expectedDate"
const val PAYMENT_SUCCESS = "paymentSuccess"

/**
 * Available variables
 */
val DelegateExecution.orderIdVar get() = (getVariable(ORDER_ID) as String).let(UUID::fromString)
val DelegateExecution.addressVar get() = getVariable(ADDRESS) as String
val DelegateExecution.loginVar get() = getVariable(LOGIN) as String
val DelegateExecution.innVar get() = getVariable(INN) as String
val DelegateExecution.accountNumberVar get() = getVariable(ACCOUNT_NUMBER) as String
val DelegateExecution.amountVar get() = (getVariable(AMOUNT) as String).toBigDecimal()
val DelegateExecution.contractIdVar get() = (getVariable(CONTRACT_ID) as String).let(UUID::fromString)
val DelegateExecution.deliveryIdVar get() = (getVariable(DELIVERY_ID) as String).let(UUID::fromString)
val DelegateExecution.expectedDateVar get() = getVariable(EXPECTED_DATE) as LocalDate
