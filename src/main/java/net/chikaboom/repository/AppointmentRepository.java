package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс для CRUD обработки таблицы Appointment
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    /**
     * Метод для поиска всех записей к мастеру
     *
     * @param masterAccount аккаунт мастера
     * @return коллекцию всех записей к указанному мастеру
     */
    List<Appointment> findAllByMasterAccount(Account masterAccount);

    /**
     * Метод для поиска всех записей, на которые записан клиент
     *
     * @param clientAccount аккаунт клиента
     * @return коллекцию всех записей, на которые записался клиент
     */
    List<Appointment> findAllByClientAccount(Account clientAccount);
}
