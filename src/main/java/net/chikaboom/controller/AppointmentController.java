package net.chikaboom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.model.database.Appointment;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PropertySource("/constants.properties")
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

    private ClientDataStorageService clientDataStorageService;
    private AppointmentService appointmentService;

    @Autowired
    public AppointmentController(ClientDataStorageService clientDataStorageService, AppointmentService appointmentService) {
        this.clientDataStorageService = clientDataStorageService;
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<Appointment> makeAppointment(@PathVariable int idAccountMaster, @RequestParam int idAccountClient,
                                          @RequestParam int idUserService, @RequestParam String appointmentDate,
                                          @RequestParam String appointmentTime) {

        clientDataStorageService.setData(ID_ACCOUNT_MASTER, idAccountMaster);
        clientDataStorageService.setData(ID_ACCOUNT_CLIENT, idAccountClient);
        clientDataStorageService.setData(ID_USER_SERVICE, idUserService);
        clientDataStorageService.setData(APPOINTMENT_DATE, appointmentDate);
        clientDataStorageService.setData(APPOINTMENT_TIME, appointmentTime);

        Appointment appointment = appointmentService.executeAndGetOne();

        clientDataStorageService.clearAllData();

        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> getAllMasterAppointments(@PathVariable int idAccountMaster) {
        clientDataStorageService.setData(ID_ACCOUNT_MASTER, idAccountMaster);

        List<Appointment> appointmentList =  appointmentService.executeAndGetList();

        clientDataStorageService.clearAllData();

        ObjectMapper mapper = new ObjectMapper();

        String appointmentListJson = "";

        try {
            appointmentListJson = mapper.writeValueAsString(appointmentList);
        } catch (JsonProcessingException e) {
//            TODO Exception
            e.printStackTrace();
        }

        return new ResponseEntity<>(appointmentListJson, HttpStatus.OK);
    }

    @DeleteMapping("/{idAppointment}")
    public ResponseEntity<String> deleteAppointment(@PathVariable int idAccountMaster, @PathVariable int idAppointment) {
        clientDataStorageService.setData(ID_APPOINTMENT, idAppointment);

        appointmentService.deleteAppointment();

        clientDataStorageService.clearAllData();

        return new ResponseEntity<>("Appointment deleted", HttpStatus.OK);
    }
}
