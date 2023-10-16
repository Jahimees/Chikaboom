package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.AppointmentFacadeConverter;
import net.chikaboom.facade.dto.AppointmentFacade;
import net.chikaboom.facade.dto.Facade;
import net.chikaboom.facade.dto.UserDetailsFacade;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Appointment;
import net.chikaboom.model.database.CustomPrincipal;
import net.chikaboom.model.database.UserDetails;
import net.chikaboom.model.response.CustomResponseObject;
import net.chikaboom.service.data.AccountDataService;
import net.chikaboom.service.data.AppointmentDataService;
import net.chikaboom.service.data.UserDetailsDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST контроллер для взаимодействия с сущностями типа {@link Appointment}
 */
@RestController
@RequiredArgsConstructor
public class AppointmentRestController {

    private final AppointmentDataService appointmentDataService;
    private final UserDetailsDataService userDetailsDataService;
    private final AccountDataService accountDataService;

    /**
     * Поиск записи по её идентификатору
     *
     * @param idAppointment идентификатор записи
     * @return json, содержащий запись
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/appointments/{idAppointment}")
    @Transactional(readOnly = true)
    public ResponseEntity<Facade> findAppointment(@PathVariable int idAppointment) {
        Optional<Appointment> appointmentOptional = appointmentDataService.findById(idAppointment);

        if (!appointmentOptional.isPresent()) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.NOT_FOUND.value(),
                    "There not found appointment with id " + idAppointment,
                    "GET:/appointments/" + idAppointment
            ), HttpStatus.NOT_FOUND);
        }

        Appointment appointmentFromDb = appointmentOptional.get();

        Account accountFacadeMaster = appointmentFromDb.getMasterAccount();
        UserDetails clientDetailsFacade = appointmentFromDb.getUserDetailsClient();

        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        if (principal.getIdAccount() != accountFacadeMaster.getIdAccount()
                && principal.getIdUserDetails() != clientDetailsFacade.getIdUserDetails()) {

            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    "It's not allowed to find not your's appointment",
                    "GET:/appointments/" + idAppointment
            ), HttpStatus.FORBIDDEN);
        }

        AppointmentFacade appointmentFacade = AppointmentFacadeConverter.convertToDto(appointmentFromDb);

        return ResponseEntity.ok(appointmentFacade);
    }

    /**
     * Производит поиск всех записей. Необходимо быть авторизованным
     *
     * @return json, содержащий все возможные записи
     * @deprecated нет необходимости в поиске всех записей
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/appointments")
    @Deprecated
    public ResponseEntity<List<AppointmentFacade>> findAllAppointments() {
        return ResponseEntity.ok(appointmentDataService.findAll()
                .stream().map(AppointmentFacadeConverter::convertToDto).collect(Collectors.toList()));
    }

    /**
     * Производит поиск входящих записей к мастеру
     *
     * @param idAccount идентификатор аккаунта мастера
     * @return список записей к указанному мастеру
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/accounts/{idAccount}/income-appointments")
    public ResponseEntity<List<AppointmentFacade>> findIncomeAppointments(@PathVariable int idAccount) {
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
    public ResponseEntity<List<AppointmentFacade>> findOutcomeAppointments(@PathVariable int idAccount) {
        return findAppointmentsByIdAccount(idAccount, true);
    }

    /**
     * Поиск всех записей клиента к определенному мастеру
     *
     * @param idMasterAccount идентификатор аккаунта мастера
     * @param idUserDetails   идентификатора информации о клиенте
     * @return список записей к мастеру
     */
    @PreAuthorize("isAuthenticated() && #idMasterAccount == authentication.principal.idAccount")
    @GetMapping("/accounts/{idMasterAccount}/appointments")
    public ResponseEntity<List<Facade>> clientAppointmentsByIdUserDetails(@PathVariable int idMasterAccount,
                                                                          @RequestParam int idUserDetails) {
        Optional<UserDetails> userDetailsOptional = userDetailsDataService.findById(idUserDetails);

        if (!userDetailsOptional.isPresent()) {
            return new ResponseEntity<>(List.of(
                    new CustomResponseObject(
                            HttpStatus.NOT_FOUND.value(),
                            "There not found userDetails with id " + idUserDetails,
                            "GET:/accounts/" + idMasterAccount + "/appointments"
                    )), HttpStatus.NOT_FOUND
            );
        }

        Optional<Account> accountOptional = accountDataService.findById(idMasterAccount);

        if (!accountOptional.isPresent()) {
            return new ResponseEntity<>(List.of(
                    new CustomResponseObject(
                            HttpStatus.NOT_FOUND.value(),
                            "There not found account with id " + idMasterAccount,
                            "GET:/accounts/" + idMasterAccount + "/appointments"
                    )), HttpStatus.NOT_FOUND
            );
        }

        List<Appointment> appointmentList = appointmentDataService.findAllByUserDetailsClientAndMasterAccount(
                userDetailsOptional.get(),
                accountOptional.get());

        return ResponseEntity.ok(appointmentList
                .stream().map(AppointmentFacadeConverter::toDtoForDataTableInModal).collect(Collectors.toList()));
    }

    /**
     * Создаёт запись на услугу
     *
     * @param appointmentFacade json-объект записи
     * @return созданную запись в виде json
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/appointments")
    public ResponseEntity<Facade> createAppointment(@RequestBody AppointmentFacade appointmentFacade) {
        UserDetailsFacade userDetailsFacade = appointmentFacade.getUserDetailsFacadeClient();
        CustomPrincipal principalAccount = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetailsFacade.getIdUserDetails() != principalAccount.getIdUserDetails()
                && appointmentFacade.getMasterAccountFacade().getIdAccount() != principalAccount.getIdAccount()) {
            CustomResponseObject errorObject = new CustomResponseObject(403,
                    "You cannot create appointment in which you are not play any role (client or master)",
                    "POST:/appointments");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorObject);
        }

        Appointment appointmentModel = AppointmentFacadeConverter.convertToModel(appointmentFacade);
        Appointment createdAppointment = appointmentDataService.create(appointmentModel);

        return ResponseEntity.ok(AppointmentFacadeConverter.convertToDto(createdAppointment));
    }

    /**
     * Изменяет существующую запись. Необходимо быть авторизованным. Нельзя изменять записи, не принадлежащие
     * аутентифицированному пользователю.
     *
     * @param idAppointment          идентификатор записи, которую нужно удалить
     * @param appointmentFacadeParam новый объект записи, который заменит старый
     * @return обновлённую запись на услугу
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/appointments/{idAppointment}")
    public ResponseEntity<Facade> replaceAppointment(@PathVariable int idAppointment,
                                                     @RequestBody AppointmentFacade appointmentFacadeParam) {

        Optional<Appointment> appointmentOptional = appointmentDataService.findById(idAppointment);

        if (!appointmentOptional.isPresent()) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.NOT_FOUND.value(),
                    "There not found appointment with id " + idAppointment,
                    "PUT:/appointments/" + idAppointment
            ), HttpStatus.NOT_FOUND);
        }

        Appointment appointmentFromDb = appointmentOptional.get();

        Account masterAccountFacade = appointmentFromDb.getMasterAccount();
        UserDetails clientDetailsFacade = appointmentFromDb.getUserDetailsClient();
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getIdAccount() != masterAccountFacade.getIdAccount()
                && principal.getIdUserDetails() != clientDetailsFacade.getIdUserDetails()
                || (masterAccountFacade.getIdAccount() != appointmentFacadeParam.getMasterAccountFacade().getIdAccount())
                || clientDetailsFacade.getIdUserDetails() == appointmentFacadeParam.getUserDetailsFacadeClient().getIdUserDetails()) {

            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    "You can't get access to appointment replacement command",
                    "PUT:/appointments/" + idAppointment
            ), HttpStatus.FORBIDDEN);
        }

        appointmentFacadeParam.setIdAppointment(idAppointment);
        Appointment updatedAppointment = appointmentDataService.update(
                AppointmentFacadeConverter.convertToModel(appointmentFacadeParam));

        return ResponseEntity.ok(AppointmentFacadeConverter.convertToDto(updatedAppointment));
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
    public ResponseEntity<Facade> deleteAppointment(@PathVariable int idAppointment) {
        Optional<Appointment> appointmentOptional = appointmentDataService.findById(idAppointment);

        if (!appointmentOptional.isPresent()) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.NOT_FOUND.value(),
                    "There not found appointment with id " + idAppointment,
                    "DELETE:/appointments/" + idAppointment
            ), HttpStatus.NOT_FOUND);
        }

        Appointment appointmentFromDb = appointmentOptional.get();


        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account masterAccount = appointmentFromDb.getMasterAccount();
        UserDetails userDetailsClient = appointmentFromDb.getUserDetailsClient();

        if (principal.getIdAccount() != masterAccount.getIdAccount()
                && principal.getIdUserDetails() != userDetailsClient.getIdUserDetails()) {

            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    "You can't delete not your appointment",
                    "DELETE:/appointments/" + idAppointment
            ), HttpStatus.FORBIDDEN);
        }

        appointmentDataService.deleteById(idAppointment);
        return ResponseEntity.ok(new CustomResponseObject(
                HttpStatus.OK.value(),
                "Appointment with id " + idAppointment + " deleted",
                "DELETE:/appointments/" + idAppointment
        ));
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
    public ResponseEntity<Facade> deleteOutcomeAppointment(@PathVariable int idAccount, @PathVariable int idAppointment) {

        Optional<Appointment> appointmentOptional = appointmentDataService.findById(idAppointment);

        if (!appointmentOptional.isPresent()) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.NOT_FOUND.value(),
                    "There not found appointment with id " + idAppointment,
                    "DELETE:/accounts/" + idAccount + "/outcome-appointments/" + idAppointment
            ), HttpStatus.NOT_FOUND);
        }

        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.NOT_FOUND.value(),
                    "There not found account with idAccount " + idAccount,
                    "DELETE:/accounts/" + idAccount + "/outcome-appointments/" + idAppointment
            ), HttpStatus.NOT_FOUND);
        }

        Appointment appointmentFromDb = appointmentOptional.get();
        Account accountFromDb = accountOptional.get();

        if (accountFromDb.getUserDetails() == null) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.NOT_FOUND.value(),
                    "There is empty userDetails in account with id " + idAccount,
                    "DELTE:/accounts/" + idAccount + "/outcome-appointments/" + idAccount
            ), HttpStatus.NOT_FOUND);
        }

        if (appointmentFromDb.getUserDetailsClient().getIdUserDetails() !=
                accountFromDb.getUserDetails().getIdUserDetails()) {

            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    "You can't delete not your appointment",
                    "DELETE:/accounts/" + idAccount + "/outcome-appointments/" + idAppointment
            ), HttpStatus.FORBIDDEN);
        }

        appointmentDataService.deleteById(idAppointment);

        return ResponseEntity.ok(new CustomResponseObject(
                HttpStatus.OK.value(),
                "Appointment with id " + idAppointment + " deleted",
                "DELETE:/appointments/" + idAppointment
        ));
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
    public ResponseEntity<Facade> deleteIncomeAppointment(@PathVariable int idAccount, @PathVariable int idAppointment) {

        Optional<Appointment> appointmentOptional = appointmentDataService.findById(idAppointment);

        if (!appointmentOptional.isPresent()) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.NOT_FOUND.value(),
                    "There not found appointment with id " + idAppointment,
                    "DELETE:/accounts/" + idAccount + "/income-appointments/" + idAppointment
            ), HttpStatus.NOT_FOUND);
        }

        if (appointmentOptional.get().getMasterAccount().getIdAccount() != idAccount) {

            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    "You can't delete not your appointment",
                    "DELETE:/accounts/" + idAccount + "/income-appointments/" + idAppointment
            ), HttpStatus.FORBIDDEN);
        }

        appointmentDataService.deleteById(idAppointment);

        return ResponseEntity.ok(new CustomResponseObject(
                HttpStatus.OK.value(),
                "Appointment with id " + idAppointment + " deleted",
                "DELETE:/accounts/" + idAccount + "/income-appointments/" + idAppointment
        ));
    }

    /**
     * Производит поиск всех записей по аккаунту. В зависимости от флага возвращает записи, в которых
     * аккаунт выступает в роли клиента или в роли мастера.
     *
     * @param idAccount идентификатор аккаунта, чьи записи нужно найти
     * @param isClient  флаг, необходимо ли переданный аккаунт рассматривать как клиента или как мастера
     * @return список записей в виде json
     */
    private ResponseEntity<List<AppointmentFacade>> findAppointmentsByIdAccount(int idAccount, boolean isClient) {
        List<Appointment> appointmentList = appointmentDataService.findAllByIdAccount(idAccount, isClient);

        return ResponseEntity.ok(appointmentList
                .stream().map(AppointmentFacadeConverter::toDtoForAppointmentDataTable).collect(Collectors.toList()));
    }
}

