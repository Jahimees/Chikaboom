package net.chikaboom.service.action;

import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Appointment;
import net.chikaboom.model.database.UserService;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.AppointmentRepository;
import net.chikaboom.repository.UserServiceRepository;
import net.chikaboom.service.ClientDataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private ClientDataStorageService clientDataStorageService;
    private AppointmentRepository appointmentRepository;
    private AccountRepository accountRepository;
    private UserServiceRepository userServiceRepository;

    @Autowired
    public AppointmentService(ClientDataStorageService clientDataStorageService, AppointmentRepository appointmentRepository,
                              AccountRepository accountRepository, UserServiceRepository userServiceRepository) {
        this.clientDataStorageService = clientDataStorageService;
        this.appointmentRepository = appointmentRepository;
        this.accountRepository = accountRepository;
        this.userServiceRepository = userServiceRepository;
    }

    @Override
    public Appointment executeAndGetOne() throws NoSuchDataException {

        int idAccountMaster = (int) clientDataStorageService.getData(ID_ACCOUNT_MASTER);
        int idAccountClient = (int) clientDataStorageService.getData(ID_ACCOUNT_CLIENT);
        int idUserService = (int) clientDataStorageService.getData(ID_USER_SERVICE);
        String appointmentDate = clientDataStorageService.getData(APPOINTMENT_DATE).toString();
        String appointmentTime = clientDataStorageService.getData(APPOINTMENT_TIME).toString();

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

    @Override
    public List<Appointment> executeAndGetList() throws NoSuchDataException {
        int idAccountMaster = (int) clientDataStorageService.getData(ID_ACCOUNT_MASTER);

        Account master = accountRepository.findById(idAccountMaster)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccountMaster));

        return appointmentRepository.findAllByMasterAccount(master);
    }

    public List<Appointment> executeAndGetClientList() throws NoSuchDataException {
        int idAccountClient = (int) clientDataStorageService.getData(ID_ACCOUNT_CLIENT);

        Account client = accountRepository.findById(idAccountClient)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccountClient));

        return appointmentRepository.findAllByClientAccount(client);
    }

    public void deleteAppointment() {
        int idAppointment = (int) clientDataStorageService.getData(ID_APPOINTMENT);

        Appointment appointment = appointmentRepository.findById(idAppointment)
                .orElseThrow(() -> new NoSuchDataException("Cannot find appointment with id " + idAppointment));;

        appointmentRepository.delete(appointment);
    }
}
