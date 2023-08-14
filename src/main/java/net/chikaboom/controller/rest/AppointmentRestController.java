package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Appointment;
import net.chikaboom.service.data.AppointmentDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//TODO проверки на совпадение id и PreAuthorize
@RestController
@RequiredArgsConstructor
public class AppointmentRestController {

    private final AppointmentDataService appointmentDataService;

    @GetMapping("/appointments/{idAppointment}")
    public ResponseEntity<Appointment> findAppointment(@PathVariable int idAppointment) {
        Optional<Appointment> accountOptional = appointmentDataService.findById(idAppointment);

        return accountOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> findAllAppointments() {
        return ResponseEntity.ok(appointmentDataService.findAll());
    }

    @PostMapping("/appointments")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentDataService.create(appointment));
    }

    @PutMapping("/appointments/{idAppointment}")
    public ResponseEntity<Appointment> replaceAppointment(@PathVariable int idAppointment, @RequestBody Appointment appointment) {
        Optional<Appointment> appointmentOptional = appointmentDataService.findById(idAppointment);

        if (!appointmentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        appointment.setIdAppointment(idAppointment);

        return ResponseEntity.ok(appointmentDataService.update(appointment));
    }

    @DeleteMapping("/appointments/{idAppointment}")
    public void deleteAppointment(@PathVariable int idAppointment) {
        appointmentDataService.deleteById(idAppointment);
//        TODO проверка на авторизованность?
    }

    @GetMapping("/accounts/{idAccount}/income-appointments")
    public ResponseEntity<List<Appointment>> findIncomeAppointments(@PathVariable int idAccount) {
        return findAppointmentsByIdAccount(idAccount, false);
    }

    @GetMapping("/accounts/{idAccount}/outcome-appointments")
    public ResponseEntity<List<Appointment>> findOutcomeAppointments(@PathVariable int idAccount) {
        return findAppointmentsByIdAccount(idAccount, true);
    }

    private ResponseEntity<List<Appointment>> findAppointmentsByIdAccount(int idAccount, boolean isClient) {
        List<Appointment> appointmentList = appointmentDataService.findAllByIdAccount(idAccount, isClient);

        return ResponseEntity.ok(appointmentList);
    }
}

