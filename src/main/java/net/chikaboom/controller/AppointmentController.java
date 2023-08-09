package net.chikaboom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.model.database.Appointment;
import net.chikaboom.service.data.AppointmentDataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Обрабатывает запросы, связанные с записями на услуги.
 */
@RestController
@RequestMapping("/chikaboom/appointment/{idAccountMaster}")
@PreAuthorize("isAuthenticated()")
public class AppointmentController {

    private final AppointmentDataService appointmentDataService;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public AppointmentController(AppointmentDataService appointmentDataService) {
        this.appointmentDataService = appointmentDataService;
    }

    /**
     * Создает запись на услугу по параметрам, выбранными клиентом.
     * Управление передается {@link AppointmentDataService} для создания данных.
     *
     * @param idAccountMaster идентификатор аккаунта мастера (к кому запись)
     * @param idAccountClient идентификатор аккаунта клиента (кто совершает запись)
     * @param idService       идентификатор пользовательской услуги, на которую совершается запись
     * @param appointmentDate дата записи
     * @param appointmentTime время записи
     * @return сохраненный объект записи
     */
    @PreAuthorize("#idAccountClient == authentication.principal.idAccount and #idAccountClient != #idAccountMaster")
    @PostMapping
    public ResponseEntity<Appointment> makeAppointment(@PathVariable int idAccountMaster, @RequestParam int idAccountClient,
                                                       @RequestParam int idService, @RequestParam String appointmentDate,
                                                       @RequestParam String appointmentTime) {
        logger.info("Making appointment for client (idClient=" + idAccountClient + ") to master (idMaster=" + idAccountMaster + ").");

        Appointment appointment = appointmentDataService.createAppointmentOld(idAccountMaster, idAccountClient, idService,
                appointmentDate, appointmentTime);

        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    /**
     * Обрабатывает запрос на получение всех записей к указанному мастеру.
     *
     * @param idAccountMaster идентификатор аккаунта мастера
     * @return все записи к мастеру в формате JSON
     */
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<String> getAllMasterAppointments(@PathVariable int idAccountMaster) {
        logger.info("Getting full info about appointments of master with id " + idAccountMaster);

        List<Appointment> appointmentList = appointmentDataService.findAllByIdAccount(idAccountMaster, false);

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
     * @param idAppointment   идентификатор записи
     * @return строку результата удаления с соответствующим HTTP-кодом
     */
    @PreAuthorize("#idAccountMaster == authentication.principal.idAccount")
    @DeleteMapping("/{idAppointment}")
    public ResponseEntity<String> deleteAppointment(@PathVariable int idAccountMaster, @PathVariable int idAppointment) {
        logger.info("Starting deleting appointment with id " + idAppointment);

        appointmentDataService.deleteById(idAppointment);

        return new ResponseEntity<>("Appointment deleted", HttpStatus.OK);
    }
}
