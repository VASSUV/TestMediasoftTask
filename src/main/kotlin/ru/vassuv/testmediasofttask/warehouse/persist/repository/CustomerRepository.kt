package ru.vassuv.testmediasofttask.warehouse.persist.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.vassuv.testmediasofttask.warehouse.persist.entity.CustomerEntity
import java.util.UUID

/**
 * Репозиторий для работы с заказчиками ([CustomerEntity]) в базе данных.
 *
 * Обеспечивает стандартные CRUD-операции и дополнительные проверки уникальности данных заказчиков.
 */
interface CustomerRepository : JpaRepository<CustomerEntity, UUID> {

    /**
     * Проверяет существование заказчика по указанному логину или email.
     *
     * Используется при регистрации новых заказчиков для предотвращения дублирования данных.
     *
     * @param login логин заказчика.
     * @param email email заказчика.
     * @return true, если заказчик с таким логином или email уже существует; иначе false.
     */
    fun existsByLoginOrEmail(login: String, email: String): Boolean

    /**
     * Находит первого заказчика по его profileId
     *
     * @param profileId идентификатор профиля заказчика.
     * @return Объект заказчика ([CustomerEntity]).
     */
    fun findFirstByProfileId(profileId: UUID): CustomerEntity?
}
