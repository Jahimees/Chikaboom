package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.*;
import net.chikaboom.repository.AppointmentRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис предоставляет возможность обработки данных пользовательских записей на услуги
 */
@RequiredArgsConstructor
@org.springframework.stereotype.Service
@Transactional
public class AppointmentDataService implements DataService<Appointment> {

    @Value("${one_hour_millis}")
    private long ONE_HOUR_MILLIS;

    private final AppointmentRepository appointmentRepository;
    private final AccountDataService accountDataService;
    private final ServiceDataService serviceDataService;
    private final WorkingDayDataService workingDayDataService;

    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    /**
     * Производит поиск записи по идентификатору.
     *
     * @param idAppointment идентификатор записи
     * @return объект записи
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Appointment> findById(int idAppointment) {
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
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointment.getIdAppointment());

        if (!appointmentOptional.isPresent()) {
            throw new NotFoundException("There not found appointment with id " + appointment.getIdAppointment());
        }

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
        Timestamp nowTime = Timestamp.valueOf(LocalDateTime.now());

        if (appointment.getAppointmentDateTime().getTime() - nowTime.getTime() < ONE_HOUR_MILLIS) {
            logger.warn("Appointment will not be created. Cause appointment date time is earlier than present time");

            throw new IllegalArgumentException("Appointment will not be created. " +
                    "Cause appointment date time is earlier than present time");
        }

        if (isAppointmentExists(appointment)) {
            throw new AlreadyExistsException("The same appointment already exists");
        }
        if (appointment.getService() == null) {
            throw new NotFoundException("Received service is null");
        }

        Timestamp appointmentDateTime = appointment.getAppointmentDateTime();

        List<WorkingDay> masterWorkingDays = workingDayDataService.findWorkingDaysByIdAccount(
                appointment.getMasterAccount().getIdAccount());

        List<WorkingDay> workingDayList = masterWorkingDays.stream().filter(workingDay ->
                        workingDay.getDate().getYear() == appointmentDateTime.getYear()
                                && workingDay.getDate().getMonth() == appointmentDateTime.getMonth()
                                && workingDay.getDate().getDate() == appointmentDateTime.getDate())
                .collect(Collectors.toList());

        if (workingDayList.isEmpty()) {
//            TODO custom handlers for REST!!!!
            throw new NotFoundException("There is no working day for current master");
        }

        WorkingDay chosenWorkingDay = workingDayList.get(0);

        //Время записи поставлено не раньше, чем начало рабочего дня
        if (!((chosenWorkingDay.getWorkingDayStart().getHours() == appointmentDateTime.getHours()
                && chosenWorkingDay.getWorkingDayStart().getMinutes() <= appointmentDateTime.getMinutes())
                || chosenWorkingDay.getWorkingDayStart().getHours() < appointmentDateTime.getHours())) {
            throw new IllegalArgumentException("You cannot create appointment earlier than working day starts");
        }

        Optional<Service> chosenMasterServiceOptional = serviceDataService.findById(appointment
                .getService().getIdService());

        if (!chosenMasterServiceOptional.isPresent()) {
            throw new NotFoundException("There not found service");
        }

        Service chosenMasterService = chosenMasterServiceOptional.get();

        int[] chosenServiceTimeNumbers = chosenMasterService.getServiceTimeNumbers();

        //Время записи поставлено так, что не выйдет за рамки конца рабочего дня
        if (!((chosenWorkingDay.getWorkingDayEnd().getHours() == appointmentDateTime.getHours() + chosenServiceTimeNumbers[0]
                && chosenWorkingDay.getWorkingDayEnd().getMinutes() >= appointmentDateTime.getMinutes() + chosenServiceTimeNumbers[1])
                || chosenWorkingDay.getWorkingDayEnd().getHours() > appointmentDateTime.getHours() + chosenServiceTimeNumbers[0])) {

            throw new IllegalArgumentException("You cannot create appointment which will finish after than working day ends");
        }

        List<Appointment> masterAppointmentList = findAllByIdAccount(appointment
                .getMasterAccount().getIdAccount(), false);

        masterAppointmentList.forEach(masterAppointment -> {
            Timestamp existAppointmentDateTime = masterAppointment.getAppointmentDateTime();
            //Совпадение дня записи с новым днём записи
            if (existAppointmentDateTime.getYear() == appointmentDateTime.getYear()
                    && existAppointmentDateTime.getMonth() == appointmentDateTime.getMonth()
                    && existAppointmentDateTime.getDate() == appointmentDateTime.getDate()) {

                //Создаем временнЫе точки начала и конца записей и проверяем, не пересекаются ли они
                int appDateTimeStart = appointmentDateTime.getHours() * 60 + appointmentDateTime.getMinutes();
                int appDateTimeEnd = appDateTimeStart + (chosenServiceTimeNumbers[0] * 60 + chosenServiceTimeNumbers[1]);

                int exAppDateTimeStart = existAppointmentDateTime.getHours() * 60 + existAppointmentDateTime.getMinutes();
                int[] exServiceTimeNumbers = masterAppointment.getService().getServiceTimeNumbers();
                int exAppDateTimeEnd = exAppDateTimeStart + (exServiceTimeNumbers[0] * 60 + exServiceTimeNumbers[1]);

                if ((appDateTimeStart >= exAppDateTimeStart && appDateTimeStart < exAppDateTimeEnd) ||
                        (appDateTimeEnd > exAppDateTimeStart && appDateTimeEnd <= exAppDateTimeEnd) ||
                        (exAppDateTimeEnd > appDateTimeStart && exAppDateTimeEnd <= appDateTimeEnd) ||
                        (exAppDateTimeStart >= appDateTimeStart && exAppDateTimeStart < appDateTimeEnd)) {

//                    TODO response in json
                    throw new IllegalArgumentException("Cannot create the appointment cause it crosses with another appointment");
                }

            }
        });

        appointment.setIdAppointment(0);
        appointment.setService(chosenMasterService);

        return appointmentRepository.saveAndFlush(appointment);
    }

    /**
     * Производит поиск по идентификатору аккаунта и возвращает все записи на услуги.
     *
     * @return коллекцию записей на услуги
     * @throws NoSuchDataException возникает, если аккаунт не был найден
     */
    public List<Appointment> findAllByIdAccount(int idAccount, boolean isClient) throws NoSuchDataException {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("There not found account with id " + idAccount);
        }

        Account account = accountOptional.get();

        List<Appointment> appointmentList;
        if (isClient) {
            appointmentList = appointmentRepository.findAllByUserDetailsClient(account.getUserDetails());
        } else {
            appointmentList = appointmentRepository.findAllByMasterAccount(account);
        }

        return appointmentList;
    }

    /**
     * Проверяет, возможно ли создать запись с указанными параметрами. Метод требует доработки
     *
     * @param appointment проверяемый объект записи
     * @return true - если запись можно создавать, false - в ином случае
     */
    public boolean isAppointmentExists(Appointment appointment) {
//        TODO проверка по времени записи
//        TODO проверка на рамки рабочего дня
        return appointmentRepository.existsByAppointmentDateTimeAndMasterAccount(
                appointment.getAppointmentDateTime(),
                appointment.getMasterAccount());
    }

    public List<Appointment> findAllByUserDetailsClientAndMasterAccount(UserDetails userDetailsClient,
                                                                        Account masterAccount) {

        return appointmentRepository.findAllByUserDetailsClientAndMasterAccount(userDetailsClient, masterAccount);
    }
}
