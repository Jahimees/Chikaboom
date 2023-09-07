package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Appointment;
import net.chikaboom.model.database.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Интерфейс для CRUD обработки таблицы Appointment
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer>, JpaSpecificationExecutor<Appointment> {

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
     * @param userDetailsClient информация о клиенте
     * @return коллекцию всех записей, на которые записался клиент
     */
    List<Appointment> findAllByUserDetailsClient(UserDetails userDetailsClient);

    /**
     * Производит поиск всех записей клиента к определенному мастеру
     * @param userDetailsClient объект информации о клиенте
     * @param accountMaster аккаунт мастера
     * @return список записей клиента
     */
    List<Appointment> findAllByUserDetailsClientAndMasterAccount(UserDetails userDetailsClient, Account accountMaster);

    /**
     * Проверяет, существует ли запись на указанное время и указанную дату у определенного мастера.
     *
     * @param appointmentDateTime дата и время записи
     * @param masterAccount       аккаунт мастера
     * @return true - если такая запись существует, false - в противном случае
     */
    boolean existsByAppointmentDateTimeAndMasterAccount(Timestamp appointmentDateTime, Account masterAccount);
}
