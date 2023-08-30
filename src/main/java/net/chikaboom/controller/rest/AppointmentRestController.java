package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Appointment;
import net.chikaboom.model.database.UserDetails;
import net.chikaboom.service.data.AccountDataService;
import net.chikaboom.service.data.AppointmentDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST контроллер для взаимодействия с сущностями типа {@link Appointment}
 */
@RestController
@RequiredArgsConstructor
public class AppointmentRestController {

    @Value("${one_hour_millis}")
    private long ONE_HOUR_MILLIS;

    private final AppointmentDataService appointmentDataService;
    private final AccountDataService accountDataService;

    /**
     * Поиск записи по её идентификатору
     *
     * @param idAppointment идентификатор записи
     * @return json, содержащий запись
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/appointments/{idAppointment}")
    public ResponseEntity<Appointment> findAppointment(@PathVariable int idAppointment) {
        Optional<Appointment> appointmentOptional = appointmentDataService.findById(idAppointment);

        if (!appointmentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Appointment appointment = appointmentOptional.get();
        Account appointmentAccountMaster = appointment.getMasterAccount();
        UserDetails clientDetails = appointment.getUserDetailsClient();

        Account principal = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getIdAccount() != appointmentAccountMaster.getIdAccount()
                && principal.getUserDetails().getIdUserDetails() != clientDetails.getIdUserDetails()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(appointmentOptional.get());
    }

    /**
     * Производит поиск всех записей. Необходимо быть авторизованным
     *
     * @return json, содержащий все возможные записи
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> findAllAppointments() {
        return ResponseEntity.ok(appointmentDataService.findAll());
    }

    /**
     * Создаёт запись на услугу
     *
     * @param appointment json-объект записи
     * @return созданную запись в виде json
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/appointments")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        UserDetails userDetails = appointment.getUserDetailsClient();
        Account principalAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails.getIdUserDetails() != principalAccount.getUserDetails().getIdUserDetails()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Timestamp nowTime = Timestamp.valueOf(LocalDateTime.now());

        if (appointment.getAppointmentDateTime().getTime() - nowTime.getTime() < ONE_HOUR_MILLIS) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(appointmentDataService.create(appointment));
    }

    /**
     * Изменяет существующую запись. Необходимо быть авторизованным. Нельзя изменять записи, не принадлежащие
     * аутентифицированному пользователю.
     *
     * @param idAppointment идентификатор записи, которую нужно удалить
     * @param appointment   новый объект записи, который заменит старый
     * @return обновлённую запись на услугу
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/appointments/{idAppointment}")
    public ResponseEntity<Appointment> replaceAppointment(@PathVariable int idAppointment, @RequestBody Appointment appointment) {
        Optional<Appointment> appointmentOptional = appointmentDataService.findById(idAppointment);

        if (!appointmentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Appointment appointmentFromDb = appointmentOptional.get();
        Account masterAccount = appointmentFromDb.getMasterAccount();
        UserDetails clientDetails = appointmentFromDb.getUserDetailsClient();
        Account principalAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principalAccount.getIdAccount() != masterAccount.getIdAccount()
                && principalAccount.getUserDetails().getIdUserDetails() != clientDetails.getIdUserDetails()
                || (masterAccount.getIdAccount() != appointment.getMasterAccount().getIdAccount())
                || clientDetails.getIdUserDetails() == appointment.getUserDetailsClient().getIdUserDetails()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        appointment.setIdAppointment(idAppointment);

        return ResponseEntity.ok(appointmentDataService.update(appointment));
    }

    /**
     * Удаляет выбранную запись на услугу. Необходимо быть авторизованным. Нельзя удалить записи, не принаждежащие
     * аутентифицированному пользователю.
     *
     * @param idAppointment идентификатор записи на услугу, которую нужно удалить
     * @return json-ответ с кодом
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/appointments/{idAppointment}")
    public ResponseEntity<String> deleteAppointment(@PathVariable int idAppointment) {
        Optional<Appointment> optionalAppointment = appointmentDataService.findById(idAppointment);

        if (!optionalAppointment.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Appointment appointment = optionalAppointment.get();
        Account principalAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account masterAccount = appointment.getMasterAccount();
        UserDetails userDetailsClient = appointment.getUserDetailsClient();

        if (principalAccount.getIdAccount() != masterAccount.getIdAccount()
                && principalAccount.getUserDetails().getIdUserDetails() != userDetailsClient.getIdUserDetails()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        appointmentDataService.deleteById(idAppointment);
        return ResponseEntity.ok().build();
    }

    /**
     * Производит поиск входящих записей к мастеру
     *
     * @param idAccount идентификатор аккаунта мастера
     * @return список записей к указанному мастеру
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/accounts/{idAccount}/income-appointments")
    public ResponseEntity<List<Appointment>> findIncomeAppointments(@PathVariable int idAccount) {
        return findAppointmentsByIdAccount(idAccount, false);
    }

    /**
     * Производит поиск исходящих записей клиента
     *
     * @param idAccount идентификатор аккаунта клиента
     * @return список записей на которые записан клиент
     */
    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @GetMapping("/accounts/{idAccount}/outcome-appointments")
    public ResponseEntity<List<Appointment>> findOutcomeAppointments(@PathVariable int idAccount) {
        return findAppointmentsByIdAccount(idAccount, true);
    }

    /**
     * Удаляет исходящую запись на услугу. Необходимо быть авторизованным. Невозможно удалить чужую запись
     *
     * @param idAccount     идентификатор аккаунта, чью запись нужно удалить
     * @param idAppointment идентификатор записи, которую нужно удалить
     * @return json-ответ с кодом
     */
    @PreAuthorize("hasRole('CLIENT') && #idAccount == authentication.principal.idAccount")
    @DeleteMapping("/accounts/{idAccount}/outcome-appointments/{idAppointment}")
    public ResponseEntity<String> deleteOutcomeAppointment(@PathVariable int idAccount, @PathVariable int idAppointment) {
        Optional<Appointment> appointmentOptional = appointmentDataService.findById(idAppointment);

        if (!appointmentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Appointment appointment = appointmentOptional.get();
        Optional<Account> accountFromDb = accountDataService.findById(idAccount);

        if (!accountFromDb.isPresent() || accountFromDb.get().getUserDetails() == null) {
            return ResponseEntity.notFound().build();
        }

        if (appointment.getUserDetailsClient().getIdUserDetails() != accountFromDb.get().getUserDetails().getIdUserDetails()) {
            return new ResponseEntity<>("You can't delete not your appointment", HttpStatus.FORBIDDEN);
        }

        appointmentDataService.deleteById(idAppointment);

        return ResponseEntity.ok("Deleted");
    }

    /**
     * Удаляет входящую запись к мастеру. Необходимо быть авторизованным и иметь роль мастера. Невозможно удалить
     * чужую запись.
     *
     * @param idAccount     идентификатор аккаунта мастера, чью запись нужно удалить
     * @param idAppointment идентификатор записи, которую нужно удалить
     * @return json-ответ с кодом
     */
    @PreAuthorize("hasRole('MASTER') && #idAccount == authentication.principal.idAccount")
    @DeleteMapping("/accounts/{idAccount}/income-appointments/{idAppointment}")
    public ResponseEntity<String> deleteIncomeAppointment(@PathVariable int idAccount, @PathVariable int idAppointment) {
        Optional<Appointment> appointmentOptional = appointmentDataService.findById(idAppointment);

        if (!appointmentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Appointment appointment = appointmentOptional.get();

        if (appointment.getMasterAccount().getIdAccount() != idAccount) {
            return new ResponseEntity<>("You can't delete not your appointment", HttpStatus.FORBIDDEN);
        }

        appointmentDataService.deleteById(idAppointment);

        return ResponseEntity.ok("Deleted");
    }

    /**
     * Производит поиск всех записей по аккаунту. В зависимости от флага возвращает записи, в которых
     * аккаунт выступает в роли клиента или в роли мастера.
     *
     * @param idAccount идентификатор аккаунта, чьи записи нужно найти
     * @param isClient  флаг, необходимо ли переданный аккаунт рассматривать как клиента или как мастера
     * @return список записей в виде json
     */
    private ResponseEntity<List<Appointment>> findAppointmentsByIdAccount(int idAccount, boolean isClient) {
        List<Appointment> appointmentList = appointmentDataService.findAllByIdAccount(idAccount, isClient);

        return ResponseEntity.ok(appointmentList);
    }
}

