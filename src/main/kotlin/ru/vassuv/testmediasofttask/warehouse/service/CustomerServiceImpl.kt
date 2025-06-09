package ru.vassuv.testmediasofttask.warehouse.service

import org.apache.kafka.clients.producer.KafkaProducer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.vassuv.testmediasofttask.warehouse.exception.CustomerAlreadyExistsException
import ru.vassuv.testmediasofttask.warehouse.persist.entity.CustomerEntity
import ru.vassuv.testmediasofttask.warehouse.persist.repository.CustomerRepository
import ru.vassuv.testmediasofttask.warehouse.service.event.KafkaTopic
import ru.vassuv.testmediasofttask.warehouse.service.model.CreatedCustomer
import java.util.*

/**
 * Реализация сервиса управления заказчиками ([CustomerService]).
 *
 * Содержит логику создания и управления заказчиками с проверкой уникальности.
 */
@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository,
    private val kafkaProducerService: KafkaProducerService
) : CustomerService {

    /**
     * Создаёт нового заказчика.
     *
     * Выполняет проверку на уникальность логина и email.
     *
     * @param request данные создаваемого заказчика ([CreatedCustomer]).
     * @return идентификатор созданного заказчика.
     *
     * @throws CustomerAlreadyExistsException если заказчик с указанным логином или email уже существует.
     */
    @Transactional
    override fun createCustomer(request: CreatedCustomer): UUID {
        if (customerRepository.existsByLoginOrEmail(request.login, request.email)) {
            throw CustomerAlreadyExistsException(request.login, request.email)
        }

        val customer = CustomerEntity(
            login = request.login,
            email = request.email,
            isActive = request.isActive
        )

        customerRepository.save(customer)

        kafkaProducerService.sendStringMessage(
            topic = KafkaTopic.TEST,
            message = request.toString(),
            key = "create customer ${customer.id!!}"
        )

        return customer.id!!
    }
}
