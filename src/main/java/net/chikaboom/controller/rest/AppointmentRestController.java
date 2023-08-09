package net.chikaboom.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Appointment;
import net.chikaboom.service.data.AppointmentDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentRestController {

    private final AppointmentDataService appointmentDataService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{idAppointment}")
    public ResponseEntity<Appointment> findAppointment(@PathVariable int idAppointment) {
        Optional<Appointment> accountOptional = appointmentDataService.findById(idAppointment);

        return accountOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> findAllAppointments() {
        return ResponseEntity.ok(appointmentDataService.findAll());
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentDataService.create(appointment));
    }

    @PutMapping("/{idAppointment}")
    public ResponseEntity<Appointment> replaceAppointment(@PathVariable int idAppointment, @RequestBody Appointment appointment) {
        Optional<Appointment> appointmentOptional = appointmentDataService.findById(idAppointment);

        if (!appointmentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(appointmentDataService.update(appointment));
    }

    @PatchMapping("/{idAppointment}")
    public ResponseEntity<Appointment> changeAppointment(@PathVariable int idAppointment, @RequestBody JsonPatch jsonPatch) {
        try {
            Appointment appointment = appointmentDataService.findById(idAppointment)
                    .orElseThrow(() -> new NoSuchDataException("Appointment with id " + idAppointment + " not found"));
            Appointment patchedAppointment = applyPatchToAppointment(jsonPatch, appointment);

            return ResponseEntity.ok(appointmentDataService.update(patchedAppointment));
        } catch (JsonProcessingException | JsonPatchException e) {
            return ResponseEntity.internalServerError().build();
        } catch (NoSuchDataException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{idAppointment}")
    public void deleteAppointment(@PathVariable int idAppointment) {
        appointmentDataService.deleteById(idAppointment);
//        TODO проверка на авторизованность?
    }

    private Appointment applyPatchToAppointment(JsonPatch jsonPatch, Appointment targetAppointment)
            throws JsonProcessingException, JsonPatchException {
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(targetAppointment, JsonNode.class));
        return objectMapper.treeToValue(patched, Appointment.class);
    }
}

