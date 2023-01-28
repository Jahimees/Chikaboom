package net.chikaboom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.model.database.Appointment;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.AppointmentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Обрабатывает запросы, связанные с записями на услуги.
 */
@RestController
@RequestMapping("/chikaboom/appointment/{idAccountMaster}")
public class AppointmentController {

    @Value("${attr.idAccountMaster}")
    private String ID_ACCOUNT_MASTER;
    @Value("${attr.idAccountClient}")
    private String ID_ACCOUNT_CLIENT;
    @Value("${attr.idUserService}")
    private String ID_USER_SERVICE;
    @Value("${attr.idAppointment}")
    private String ID_APPOINTMENT;
    @Value("${attr.appointmentDate}")
    private String APPOINTMENT_DATE;
    @Value("${attr.appointmentTime}")
    private String APPOINTMENT_TIME;

    private final ClientDataStorageService clientDataStorageService;
    private final AppointmentService appointmentService;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public AppointmentController(ClientDataStorageService clientDataStorageService, AppointmentService appointmentService) {
        this.clientDataStorageService = clientDataStorageService;
        this.appointmentService = appointmentService;
    }

    /**
     * Создает запись на услугу по параметрам, выбранными клиентом.
     * Данные сохраняются в {@link ClientDataStorageService} и управление передается {@link AppointmentService} для
     * сохранения данных.
     *
     * @param idAccountMaster идентификатор аккаунта мастера (к кому запись)
     * @param idAccountClient идентификатор аккаунта клиента (кто совершает запись)
     * @param idUserService   идентификатор пользовательской услуги, на которую совершается запись
     * @param appointmentDate дата записи
     * @param appointmentTime время записи
     * @return сохраненный объект записи
     */
    @PostMapping
    public ResponseEntity<Appointment> makeAppointment(@PathVariable int idAccountMaster, @RequestParam int idAccountClient,
                                                       @RequestParam int idUserService, @RequestParam String appointmentDate,
                                                       @RequestParam String appointmentTime) {
        logger.info("Making appointment for client (idClient=" + idAccountClient + ") to master (idMaster=" + idAccountMaster + ").");
        clientDataStorageService.setData(ID_ACCOUNT_MASTER, idAccountMaster);
        clientDataStorageService.setData(ID_ACCOUNT_CLIENT, idAccountClient);
        clientDataStorageService.setData(ID_USER_SERVICE, idUserService);
        clientDataStorageService.setData(APPOINTMENT_DATE, appointmentDate);
        clientDataStorageService.setData(APPOINTMENT_TIME, appointmentTime);

        Appointment appointment = appointmentService.executeAndGetOne();

        clientDataStorageService.clearAllData();

        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    /**
     * Обрабатывает запрос на получение всех записей к указанному мастеру.
     *
     * @param idAccountMaster идентификатор аккаунта мастера
     * @return все записи к мастеру в формате JSON
     */
    @GetMapping
    public ResponseEntity<String> getAllMasterAppointments(@PathVariable int idAccountMaster) {
        logger.info("Getting full info about appointments of master with id " + idAccountMaster);
        clientDataStorageService.setData(ID_ACCOUNT_MASTER, idAccountMaster);

        List<Appointment> appointmentList = appointmentService.executeAndGetList();

        clientDataStorageService.clearAllData();

        ObjectMapper mapper = new ObjectMapper();

        String appointmentListJson = "";

        try {
            logger.info("Trying to convert appointmentList to JSON format");
            appointmentListJson = mapper.writeValueAsString(appointmentList);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        return new ResponseEntity<>(appointmentListJson, HttpStatus.OK);
    }

    /**
     * Обрабатывает запрос на удаление записи клиента к мастеру //TODO доработка уведомления клиента и мастера
     *
     * @param idAccountMaster идентификатор аккаунта мастера
     * @param idAppointment идентификатор записи
     * @return строку результата удаления с соответствующим HTTP-кодом
     */
    @DeleteMapping("/{idAppointment}")
    public ResponseEntity<String> deleteAppointment(@PathVariable int idAccountMaster, @PathVariable int idAppointment) {
        logger.info("Starting deleting appointment with id " + idAppointment);
        clientDataStorageService.setData(ID_APPOINTMENT, idAppointment);

        appointmentService.deleteAppointment();

        clientDataStorageService.clearAllData();

        return new ResponseEntity<>("Appointment deleted", HttpStatus.OK);
    }
}
