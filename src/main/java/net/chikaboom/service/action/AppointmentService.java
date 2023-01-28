package net.chikaboom.service.action;

import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Appointment;
import net.chikaboom.model.database.UserService;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.AppointmentRepository;
import net.chikaboom.repository.UserServiceRepository;
import net.chikaboom.service.ClientDataStorageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис предоставляет возможность обработки данных пользовательских записей на услуги
 */
@Service
public class AppointmentService implements DataService {

    @Value("${attr.idAppointment}")
    private String ID_APPOINTMENT;
    @Value("${attr.idAccountMaster}")
    private String ID_ACCOUNT_MASTER;
    @Value("${attr.idAccountClient}")
    private String ID_ACCOUNT_CLIENT;
    @Value("${attr.idUserService}")
    private String ID_USER_SERVICE;
    @Value("${attr.appointmentDate}")
    private String APPOINTMENT_DATE;
    @Value("${attr.appointmentTime}")
    private String APPOINTMENT_TIME;

    private final ClientDataStorageService clientDataStorageService;
    private final AppointmentRepository appointmentRepository;
    private final AccountRepository accountRepository;
    private final UserServiceRepository userServiceRepository;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public AppointmentService(ClientDataStorageService clientDataStorageService, AppointmentRepository appointmentRepository,
                              AccountRepository accountRepository, UserServiceRepository userServiceRepository) {
        this.clientDataStorageService = clientDataStorageService;
        this.appointmentRepository = appointmentRepository;
        this.accountRepository = accountRepository;
        this.userServiceRepository = userServiceRepository;
    }

    /**
     * Осуществляет сохранение записи на услугу
     *
     * @return сохраненную запись
     * @throws NoSuchDataException возникает, если по одному из переданных параметров не была найдена информация
     */
    @Override
    public Appointment executeAndGetOne() throws NoSuchDataException {
        int idAccountMaster = (int) clientDataStorageService.getData(ID_ACCOUNT_MASTER);
        int idAccountClient = (int) clientDataStorageService.getData(ID_ACCOUNT_CLIENT);
        int idUserService = (int) clientDataStorageService.getData(ID_USER_SERVICE);
        String appointmentDate = clientDataStorageService.getData(APPOINTMENT_DATE).toString();
        String appointmentTime = clientDataStorageService.getData(APPOINTMENT_TIME).toString();

        logger.info("Saving appointment for master (idAccountMaster=" + idAccountMaster + ") and for client (idAccountClient=" + idAccountClient + ").");

        Account master = accountRepository.findById(idAccountMaster)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccountMaster));
        Account client = accountRepository.findById(idAccountClient)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccountClient));
        UserService userService = userServiceRepository.findById(idUserService)
                .orElseThrow(() -> new NoSuchDataException("Cannot find userService with id " + idUserService));

        Appointment appointment = new Appointment();

        if (clientDataStorageService.getData(ID_APPOINTMENT) != null) {
            appointment.setIdAppointment((int) clientDataStorageService.getData(ID_APPOINTMENT));
        }

        appointment.setMasterAccount(master);
        appointment.setClientAccount(client);
        appointment.setUserService(userService);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentTime(appointmentTime);

        return appointmentRepository.saveAndFlush(appointment);
    }

    /**
     * Производит поиск по идентификатору аккаунта мастера и возвращает все записи к указанному мастеру.
     *
     * @return коллекцию записей мастера
     * @throws NoSuchDataException возникает, если аккаунт мастера не был найден
     */
    @Override
    public List<Appointment> executeAndGetList() throws NoSuchDataException {
        int idAccountMaster = (int) clientDataStorageService.getData(ID_ACCOUNT_MASTER);
        logger.info("Searching master appointments for master with id " + idAccountMaster);

        Account master = accountRepository.findById(idAccountMaster)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccountMaster));

        return appointmentRepository.findAllByMasterAccount(master);
    }

    /**
     * Производит поиск по идентификатору аккаунта клиента и возвращает все записи клиента на услуги.
     *
     * @return коллецию записей клиента
     * @throws NoSuchDataException возникает, если аккаунт клиента не был найден
     */
    public List<Appointment> executeAndGetClientList() throws NoSuchDataException {
        int idAccountClient = (int) clientDataStorageService.getData(ID_ACCOUNT_CLIENT);
        logger.info("Searching client appointments for client with id " + idAccountClient);

        Account client = accountRepository.findById(idAccountClient)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccountClient));

        return appointmentRepository.findAllByClientAccount(client);
    }

    /**
     * Удаляет выбранную запись //TODO уведомление клиенту и мастеру
     */
    public void deleteAppointment() {
        int idAppointment = (int) clientDataStorageService.getData(ID_APPOINTMENT);
        logger.info("Deleting appointment with id " + idAppointment);

        Appointment appointment = appointmentRepository.findById(idAppointment)
                .orElseThrow(() -> new NoSuchDataException("Cannot find appointment with id " + idAppointment));
        ;

        appointmentRepository.delete(appointment);
    }
}
