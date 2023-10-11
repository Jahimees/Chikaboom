package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.facade.converter.AccountFacadeConverter;
import net.chikaboom.facade.converter.AppointmentFacadeConverter;
import net.chikaboom.facade.converter.UserDetailsFacadeConverter;
import net.chikaboom.facade.dto.*;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Appointment;
import net.chikaboom.model.database.UserDetails;
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
public class AppointmentDataService implements DataService<AppointmentFacade> {

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
    public AppointmentFacade findById(int idAppointment) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(idAppointment);

        if (!appointmentOptional.isPresent()) {
            throw new NotFoundException("There not found appointment with id " + idAppointment);
        }

        return AppointmentFacadeConverter.convertToDto(appointmentOptional.get());
    }

    /**
     * Производит поиск всех записей.
     *
     * @return список всех существующих записей
     */
    @Override
    public List<AppointmentFacade> findAll() {
        return appointmentRepository.findAll()
                .stream().map(AppointmentFacadeConverter::convertToDto).collect(Collectors.toList());
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
     * @param appointmentFacade обновленный объект записи
     * @return сохранённый объект записи
     */
    @Override
    public AppointmentFacade update(AppointmentFacade appointmentFacade) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentFacade.getIdAppointment());

        if (!appointmentOptional.isPresent()) {
            throw new NotFoundException("There not found appointment with id " + appointmentFacade.getIdAppointment());
        }

        return AppointmentFacadeConverter.convertToDto(
                appointmentRepository.save(
                        AppointmentFacadeConverter.convertToModel(appointmentFacade)));
    }

    /**
     * Создаёт объект записи на услугу в базе
     *
     * @param appointmentFacade создаваемый объект
     * @return созданный объект записи на услугу
     */
    @Override
    public AppointmentFacade create(AppointmentFacade appointmentFacade) {
        Timestamp nowTime = Timestamp.valueOf(LocalDateTime.now());

        if (appointmentFacade.getAppointmentDateTime().getTime() - nowTime.getTime() < ONE_HOUR_MILLIS) {
            logger.warn("Appointment will not be created. Cause appointment date time is earlier than present time");

            throw new IllegalArgumentException("Appointment will not be created. " +
                    "Cause appointment date time is earlier than present time");
        }

        if (isAppointmentExists(appointmentFacade)) {
            throw new AlreadyExistsException("The same appointment already exists");
        }
        if (appointmentFacade.getServiceFacade() == null) {
            throw new NotFoundException("Received service is null");
        }

        Timestamp appointmentDateTime = appointmentFacade.getAppointmentDateTime();

        List<WorkingDayFacade> masterWorkingDays = workingDayDataService.findWorkingDaysByIdAccount(
                appointmentFacade.getMasterAccountFacade().getIdAccount());

        List<WorkingDayFacade> workingDayList = masterWorkingDays.stream().filter(workingDay ->
                        workingDay.getDate().getYear() == appointmentDateTime.getYear()
                                && workingDay.getDate().getMonth() == appointmentDateTime.getMonth()
                                && workingDay.getDate().getDate() == appointmentDateTime.getDate())
                .collect(Collectors.toList());

        if (workingDayList.isEmpty()) {
//            TODO custom handlers for REST!!!!
            throw new NotFoundException("There is no working day for current master");
        }

        WorkingDayFacade chosenWorkingDay = workingDayList.get(0);

        //Время записи поставлено не раньше, чем начало рабочего дня
        if (!((chosenWorkingDay.getWorkingDayStart().getHours() == appointmentDateTime.getHours()
                && chosenWorkingDay.getWorkingDayStart().getMinutes() <= appointmentDateTime.getMinutes())
                || chosenWorkingDay.getWorkingDayStart().getHours() < appointmentDateTime.getHours())) {
            throw new IllegalArgumentException("You cannot create appointment earlier than working day starts");
        }

        ServiceFacade chosenMasterService = serviceDataService.findById(appointmentFacade
                .getServiceFacade().getIdService());
        int[] chosenServiceTimeNumbers = chosenMasterService.getServiceTimeNumbers();

        //Время записи поставлено так, что не выйдет за рамки конца рабочего дня
        if (!((chosenWorkingDay.getWorkingDayEnd().getHours() == appointmentDateTime.getHours() + chosenServiceTimeNumbers[0]
                && chosenWorkingDay.getWorkingDayEnd().getMinutes() >= appointmentDateTime.getMinutes() + chosenServiceTimeNumbers[1])
                || chosenWorkingDay.getWorkingDayEnd().getHours() > appointmentDateTime.getHours() + chosenServiceTimeNumbers[0])) {

            throw new IllegalArgumentException("You cannot create appointment which will finish after than working day ends");
        }

        List<AppointmentFacade> masterAppointmentList = findAllByIdAccount(appointmentFacade
                .getMasterAccountFacade().getIdAccount(), false);

        masterAppointmentList.forEach(masterAppointmentFacade -> {
            Timestamp existAppointmentDateTime = masterAppointmentFacade.getAppointmentDateTime();
            //Совпадение дня записи с новым днём записи
            if (existAppointmentDateTime.getYear() == appointmentDateTime.getYear()
                    && existAppointmentDateTime.getMonth() == appointmentDateTime.getMonth()
                    && existAppointmentDateTime.getDate() == appointmentDateTime.getDate()) {

                //Создаем временнЫе точки начала и конца записей и проверяем, не пересекаются ли они
                int appDateTimeStart = appointmentDateTime.getHours() * 60 + appointmentDateTime.getMinutes();
                int appDateTimeEnd = appDateTimeStart + (chosenServiceTimeNumbers[0] * 60 + chosenServiceTimeNumbers[1]);

                int exAppDateTimeStart = existAppointmentDateTime.getHours() * 60 + existAppointmentDateTime.getMinutes();
                int[] exServiceTimeNumbers = masterAppointmentFacade.getServiceFacade().getServiceTimeNumbers();
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

        appointmentFacade.setIdAppointment(0);
        appointmentFacade.setServiceFacade(chosenMasterService);
        return AppointmentFacadeConverter.convertToDto(
                appointmentRepository.saveAndFlush(
                        AppointmentFacadeConverter.convertToModel(appointmentFacade)));
    }

    /**
     * Производит поиск по идентификатору аккаунта и возвращает все записи на услуги.
     *
     * @return коллекцию записей на услуги
     * @throws NoSuchDataException возникает, если аккаунт не был найден
     */
    public List<AppointmentFacade> findAllByIdAccount(int idAccount, boolean isClient) throws NoSuchDataException {
        AccountFacade accountFacade = accountDataService.findById(idAccount);
        Account accountModel = AccountFacadeConverter.convertToModel(accountFacade);

        List<Appointment> appointmentList;
        if (isClient) {
            appointmentList = appointmentRepository.findAllByUserDetailsClient(accountModel.getUserDetails());
        } else {
            appointmentList = appointmentRepository.findAllByMasterAccount(accountModel);
        }

        return appointmentList.stream().map(AppointmentFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    /**
     * Проверяет, возможно ли создать запись с указанными параметрами. Метод требует доработки
     *
     * @param appointmentFacade проверяемый объект записи
     * @return true - если запись можно создавать, false - в ином случае
     */
    public boolean isAppointmentExists(AppointmentFacade appointmentFacade) {
//        TODO проверка по времени записи
//        TODO проверка на рамки рабочего дня
        return appointmentRepository.existsByAppointmentDateTimeAndMasterAccount(
                appointmentFacade.getAppointmentDateTime(),
                AccountFacadeConverter.convertToModel(appointmentFacade.getMasterAccountFacade()));
    }

    public List<AppointmentFacade> findAllByUserDetailsClientAndMasterAccount(UserDetailsFacade userDetailsClientFacade,
                                                                              AccountFacade masterAccountFacade) {
        UserDetails userDetailsClient = UserDetailsFacadeConverter.convertToModel(userDetailsClientFacade);
        Account masterAccount = AccountFacadeConverter.convertToModel(masterAccountFacade);

        return appointmentRepository.findAllByUserDetailsClientAndMasterAccount(userDetailsClient, masterAccount)
                .stream().map(AppointmentFacadeConverter::convertToDto).collect(Collectors.toList());
    }
}
