package net.chikaboom.service.action;

import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Appointment;
import net.chikaboom.model.database.Service;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.AppointmentRepository;
import net.chikaboom.repository.ServiceRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Сервис предоставляет возможность обработки данных пользовательских записей на услуги
 */
@org.springframework.stereotype.Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AccountRepository accountRepository;
    private final ServiceRepository serviceRepository;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              AccountRepository accountRepository, ServiceRepository serviceRepository) {
        this.appointmentRepository = appointmentRepository;
        this.accountRepository = accountRepository;
        this.serviceRepository = serviceRepository;
    }

    /**
     * TODO NEW изменение? UPDATE операция
     * Осуществляет создание записи на услугу
     *
     * @return сохраненную запись
     * @throws NoSuchDataException возникает, если по одному из переданных параметров не была найдена информация
     */
    public Appointment createAppointment(int idAccountMaster, int idAccountClient, int idService, String appointmentDate,
                                         String appointmentTime) throws NoSuchDataException {

        logger.info("Saving appointment for master (idAccountMaster=" + idAccountMaster + ") and for client (idAccountClient=" + idAccountClient + ").");

        Account master = accountRepository.findById(idAccountMaster)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccountMaster));
        Account client = accountRepository.findById(idAccountClient)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccountClient));
        Service service = serviceRepository.findById(idService)
                .orElseThrow(() -> new NoSuchDataException("Cannot find service with id " + idService));

        Appointment appointment = new Appointment();

        appointment.setMasterAccount(master);
        appointment.setClientAccount(client);
        appointment.setService(service);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentTime(appointmentTime);

        return appointmentRepository.saveAndFlush(appointment);
    }

    /**
     * Производит поиск по идентификатору аккаунта и возвращает все записи на услуги.
     *
     * @return коллекцию записей на услуги
     * @throws NoSuchDataException возникает, если аккаунт не был найден
     */
    public List<Appointment> findAllByIdAccount(int idAccount, boolean isClient) {
        logger.info("Searching master appointments for master with id " + idAccount);

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
     * Удаляет выбранную запись по её идентификатору //TODO уведомление клиенту и мастеру
     */
    public void deleteAppointment(int idAppointment) {
        logger.info("Deleting appointment with id " + idAppointment);

        Appointment appointment = appointmentRepository.findById(idAppointment)
                .orElseThrow(() -> new NoSuchDataException("Cannot find appointment with id " + idAppointment));

        appointmentRepository.delete(appointment);
    }
}