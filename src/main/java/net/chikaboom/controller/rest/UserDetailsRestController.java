package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.Facade;
import net.chikaboom.facade.dto.UserDetailsFacade;
import net.chikaboom.model.database.CustomPrincipal;
import net.chikaboom.model.database.UserDetails;
import net.chikaboom.model.response.CustomResponseObject;
import net.chikaboom.service.data.AccountDataService;
import net.chikaboom.service.data.UserDetailsDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для взаимодействия с сущностями типа {@link UserDetails}
 */
@RestController
@RequiredArgsConstructor
public class UserDetailsRestController {

    private final UserDetailsDataService userDetailsDataService;
    private final AccountDataService accountDataService;

    /**
     * Создает данные пользователя. Разрешено к использованию только мастерам.
     *
     * @param userDetailsFacade создаваемые данные пользователя
     * @return созданные данные пользователя
     */
    @PreAuthorize("hasRole('MASTER')")
    @PostMapping("/user-details")
    public ResponseEntity<Facade> createUserDetails(@RequestBody UserDetailsFacade userDetailsFacade) {
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetailsFacade.getIdUserDetails() != 0) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.BAD_REQUEST.value(),
                    "userDetails shouldn't have an id",
                    "POST:/user-details"
            ), HttpStatus.BAD_REQUEST);
        }

        if (userDetailsFacade.getMasterOwnerFacade() == null ||
                userDetailsFacade.getMasterOwnerFacade().getIdAccount() != principal.getIdAccount()) {

            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    "User details doesn't have masterOwner id or not matches with authenticated account",
                    "POST:/user-details"
            ), HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(userDetailsDataService.create(userDetailsFacade));
    }

    /**
     * Изменяет данные пользовательских данных. Можно использовать только авторизованному пользователю, мастеру.
     * Возможно воздействовать только на те сущности, которые принадлежат аккаунту мастера.
     *
     * @param idUserDetails          идентификатор пользовательской информации
     * @param userDetailsFacadeParam новый обновленный объект к сохранению
     * @return обновленный объект
     */
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/user-details/{idUserDetails}")
    public ResponseEntity<Facade> patchUserDetails(@PathVariable int idUserDetails,
                                                   @RequestBody UserDetailsFacade userDetailsFacadeParam) {
        UserDetailsFacade userDetailsFacadeDb = userDetailsDataService.findById(idUserDetails);

        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetailsFacadeDb.getMasterOwnerFacade() == null ||
                userDetailsFacadeDb.getMasterOwnerFacade().getIdAccount() != principal.getIdAccount()) {

            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    "Changing someone else's userDetails not allowed",
                    "PATCH:/user-details/" + idUserDetails
            ), HttpStatus.FORBIDDEN);
        }

        userDetailsFacadeParam.setIdUserDetails(idUserDetails);

        return ResponseEntity.ok(userDetailsDataService.patch(userDetailsFacadeParam));
    }

    /**
     * Производит поиск пользовательской информации о клиентах конкретного мастера
     *
     * @param idAccount идентификатор аккаунта мастера, чьих клиентов нужно найти
     * @return список найденных клиентов
     */
    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @GetMapping("/accounts/{idAccount}/clients")
    public ResponseEntity<List<? extends Facade>> findClients(@PathVariable int idAccount) {
        if (!accountDataService.isAccountExistsById(idAccount)) {
            return new ResponseEntity<>(List.of(new CustomResponseObject(
                    HttpStatus.NOT_FOUND.value(),
                    "There not found account with id " + idAccount,
                    "GET:/accounts/" + idAccount + "/clients"
            )),
                    HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(userDetailsDataService.findClientsWithExtraInfo(idAccount));
    }

    /**
     * Удаляет выбранную пользовательскую информацию. Удалять пользовательскую информацию может только владелец
     *
     * @param idAccount     идентификатор мастера-владельца пользовательской информации
     * @param idUserDetails идентификтор пользовательской информации
     * @return json-ответ сервера
     */
    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @DeleteMapping("/accounts/{idAccount}/clients/{idUserDetails}")
    public ResponseEntity<Facade> deleteUserDetails(@PathVariable int idAccount, @PathVariable int idUserDetails) {
        AccountFacade accountFacade = accountDataService.findById(idAccount);

        UserDetailsFacade userDetailsFacade = userDetailsDataService.findById(idUserDetails);

        if (userDetailsFacade.getMasterOwnerFacade().getIdAccount() != accountFacade.getIdAccount()) {

            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    "You can't delete someone else's userDetails",
                    "DELETE:/accounts/" + idAccount + "/clients/" + idUserDetails
            ), HttpStatus.FORBIDDEN);
        }

        userDetailsDataService.deleteById(idUserDetails);

        return ResponseEntity.ok(new CustomResponseObject(
                HttpStatus.OK.value(),
                "UserDetails with id " + idUserDetails + " deleted",
                "DELETE:/accounts/" + idAccount + "/clients/" + idUserDetails
        ));
    }
}
