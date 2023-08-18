package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
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

    /**
     * Проверяет, существует ли запись на указанное время и указанную дату у определенного мастера.
     *
     * @param appointmentDateTime дата и время записи
     * @param masterAccount       аккаунт мастера
     * @return true - если такая запись существует, false - в противном случае
     */
    boolean existsByAppointmentDateTimeAndMasterAccount(Timestamp appointmentDateTime, Account masterAccount);
}
