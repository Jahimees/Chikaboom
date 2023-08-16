package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Appointment;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.AppointmentRepository;
import org.apache.log4j.Logger;
import org.springframework.security.acls.model.AlreadyExistsException;

import java.util.List;
import java.util.Optional;

/**
 * Сервис предоставляет возможность обработки данных пользовательских записей на услуги
 */
@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class AppointmentDataService implements DataService<Appointment> {

    private final AppointmentRepository appointmentRepository;
    private final AccountRepository accountRepository;
    private final Logger logger = Logger.getLogger(this.getClass());


    /**
     * Производит поиск записи по идентификатору.
     *
     * @param idAppointment идентификатор записи
     * @return объект записи
     */
    @Override
    public Optional<Appointment> findById(int idAppointment) {
        logger.info("Loading appointment info with id " + idAppointment);

        return appointmentRepository.findById(idAppointment);
    }

    /**
     * Производит поиск всех записей.
     *
     * @return список всех существующих записей
     */
    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    /**
     * Удаляет выбранную запись по её идентификатору. //TODO уведомление клиенту и мастеру
     */
    @Override
    public void deleteById(int idAppointment) {
        appointmentRepository.deleteById(idAppointment);
    }

    /**
     * Обновляет (полностью перезаписывает) запись на услугу в базе данных
     *
     * @param appointment обновленный объект записи
     * @return сохранённый объект записи
     */
    @Override
    public Appointment update(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    /**
     * Создаёт объект записи на услугу в базе
     *
     * @param appointment создаваемый объект
     * @return созданный объект записи на услугу
     */
    @Override
    public Appointment create(Appointment appointment) {
        if (isAppointmentExists(appointment)) {
            throw new AlreadyExistsException("The same appointment already exists");
        }
        appointment.setIdAppointment(0);

        return appointmentRepository.save(appointment);
    }

    /**
     * Производит поиск по идентификатору аккаунта и возвращает все записи на услуги.
     *
     * @return коллекцию записей на услуги
     * @throws NoSuchDataException возникает, если аккаунт не был найден
     */
    public List<Appointment> findAllByIdAccount(int idAccount, boolean isClient) throws NoSuchDataException {
        logger.info("Searching appointments for account with id " + idAccount);

        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));

        List<Appointment> appointmentList;
        if (isClient) {
            appointmentList = appointmentRepository.findAllByClientAccount(account);
        } else {
            appointmentList = appointmentRepository.findAllByMasterAccount(account);
        }

        return appointmentList;
    }

    /**
     * Проверяет, возможно ли создать запись с указанными параметрами. Метод требует доработки
     *
     * @param appointment проверяемы объект записи
     * @return true - если запись можно создавать, false - в ином случае
     */
    public boolean isAppointmentExists(Appointment appointment) {
//        TODO проверка по времени записи
        return appointmentRepository.existsByAppointmentDateAndAppointmentTimeAndMasterAccount(
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getMasterAccount());
    }
}
