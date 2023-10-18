package net.chikaboom.controller.rest;

import com.google.i18n.phonenumbers.NumberParseException;
import lombok.RequiredArgsConstructor;
import net.chikaboom.controller.RegistrationController;
import net.chikaboom.facade.converter.AccountFacadeConverter;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.Facade;
import net.chikaboom.facade.service.AccountFacadeService;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.CustomPrincipal;
import net.chikaboom.model.response.CustomResponseObject;
import net.chikaboom.service.data.AccountDataService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST контроллер для взаимодействия с сущностями типа {@link Account}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountRestController {

    private final AccountDataService accountDataService;
    private final AccountFacadeService accountFacadeService;

    /**
     * Производит поиск аккаунта по его id
     *
     * @param idAccount идентификатор аккаунта
     * @return найденный аккаунт в формате json
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/{idAccount}")
    public ResponseEntity<Facade> findAccount(@PathVariable int idAccount) {
        return ResponseEntity.ok(accountFacadeService.findById(idAccount));
    }

    /**
     * Производит поиск всех аккаунтов. Разрешено к использованию только администраторам
     *
     * @return возвращает все аккаунты
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AccountFacade>> findAllAccounts() {

        List<AccountFacade> accountFacades = accountFacadeService.findAll();

        return new ResponseEntity<>(accountFacades, HttpStatus.OK);
    }

    /**
     * Создает аккаунт. Разрешено к использованию только администраторам.
     * Для создания аккаунта см. {@link RegistrationController}.
     *
     * @param accountFacade создаваемый аккаунт
     * @return созданный аккаунта
     */
    @Deprecated
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AccountFacade> createAccount(@RequestBody AccountFacade accountFacade) {

        AccountFacade resultAccountFacade = accountFacadeService.create(accountFacade);

        return ResponseEntity.ok(resultAccountFacade);
    }

    /**
     * Изменяет выбранное(ые) поле(я) аккаунта. Необходимо быть авторизованным. Невозможно изменять чужой аккаунт.
     * Некоторые поля (например дату регистрации) изменить невозможно.
     *
     * @param idAccount идентификатор изменяемого аккаунта
     * @return сохраненный объект в виде json
     */
    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @PatchMapping("/{idAccount}")
    public ResponseEntity<Facade> changeAccount(@PathVariable int idAccount, @RequestBody AccountFacade accountFacade) {
        accountFacade.setIdAccount(idAccount);
        Account accountModel = AccountFacadeConverter.convertToModel(accountFacade);

        try {
            if (!accountDataService.isAccountExists(accountModel)) {
                return new ResponseEntity<>(new CustomResponseObject(
                        404,
                        "Account does not exist", "PATCH:/accouts/" + idAccount),
                        HttpStatus.NOT_FOUND);
            }
        } catch (NumberParseException e) {
            throw new IllegalArgumentException("There is illegal phone number arguments", e);
        }

        AccountFacade accountFacadeFromDb = AccountFacadeConverter.convertToDto(accountDataService.findById(idAccount).get());

        if (!isAuthorized(idAccount)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        accountModel.setIdAccount(idAccount);
        Account patchedAccount;
        try {
            patchedAccount = accountDataService.patch(accountModel);
        } catch (NumberParseException e) {
            return ResponseEntity.badRequest().build();
        }

        if (!patchedAccount.getUsername().equals(accountFacadeFromDb.getUsername()) ||
                !patchedAccount.getUserDetails().getPhone().equals(accountFacadeFromDb.getUserDetailsFacade().getPhone()) ||
                !patchedAccount.getPassword().equals(accountFacadeFromDb.getPassword())) {

            URI location;
            try {
                location = new URI("redirect:/logout");
            } catch (URISyntaxException e) {
                return new ResponseEntity<>(new CustomResponseObject(
                        HttpStatus.NOT_FOUND.value(),
                        "Not found logout page... WTF. Just.. Just get away please",
                        "/logout"
                ), HttpStatus.NOT_FOUND);
            }
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(location);
            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        }

        return ResponseEntity.ok(convertToDto(idAccount, patchedAccount));
    }

    //    TODO LOGOUT!!!!

    /**
     * Производит удаление аккаунта. Доступно только администраторам.
     *
     * @param idAccount идентификатор удаляемого аккаунта
     * @return json-ответ с кодом
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idAccount}")
    public ResponseEntity<String> deleteAccount(@PathVariable int idAccount) {
        accountDataService.deleteById(idAccount);

        return ResponseEntity.ok().build();
    }

    private boolean isAuthorized(int idAccount) {
        Object customPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return customPrincipal.getClass() != String.class &&
                ((CustomPrincipal) customPrincipal).getIdAccount() == idAccount;
    }

    private AccountFacade convertToDto(int idAccount, Account account) {
        CustomPrincipal customPrincipal;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String) {
            customPrincipal = new CustomPrincipal(0, 0);
        } else {
            customPrincipal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        return customPrincipal.getIdAccount() == idAccount ? AccountFacadeConverter
                .toDtoForAccountUser(account) : AccountFacadeConverter.toDtoForNotAccountUser(account);
    }
}
