package net.chikaboom.controller.rest;

import com.google.i18n.phonenumbers.NumberParseException;
import lombok.RequiredArgsConstructor;
import net.chikaboom.controller.RegistrationController;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.Facade;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.CustomPrincipal;
import net.chikaboom.model.response.CustomResponseObject;
import net.chikaboom.service.data.AccountDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для взаимодействия с сущностями типа {@link Account}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountRestController {

    private final AccountDataService accountDataService;

    /**
     * Производит поиск аккаунта по его id
     *
     * @param idAccount идентификатор аккаунта
     * @return найденный аккаунт в формате json
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/{idAccount}")
    public ResponseEntity<AccountFacade> findAccount(@PathVariable int idAccount) {
        AccountFacade accountFacade = accountDataService.findById(idAccount);

        return ResponseEntity.ok(accountFacade);
    }

    /**
     * Производит поиск всех аккаунтов. Разрешено к использованию только администраторам
     *
     * @return возвращает все аккаунты
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AccountFacade>> findAllAccounts() {
        return new ResponseEntity<>(accountDataService.findAll(), HttpStatus.OK);
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
        return ResponseEntity.ok(accountDataService.create(accountFacade));
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

        try {
            if (!accountDataService.isAccountExists(accountFacade)) {
                return new ResponseEntity<>(new CustomResponseObject(
                        404,
                        "Account does not exist", "PATCH:/accouts/" + idAccount),
                        HttpStatus.NOT_FOUND);
            }
        } catch (NumberParseException e) {
            throw new IllegalArgumentException("There is illegal phone number arguments", e);
        }

        if (!isAuthorized(idAccount)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        accountFacade.setIdAccount(idAccount);
        AccountFacade patchedAccountFacade;
        try {
            patchedAccountFacade = accountDataService.patch(accountFacade);
        } catch (NumberParseException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(patchedAccountFacade);
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
}
