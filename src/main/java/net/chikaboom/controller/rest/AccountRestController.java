package net.chikaboom.controller.rest;

import com.google.i18n.phonenumbers.NumberParseException;
import lombok.RequiredArgsConstructor;
import net.chikaboom.controller.RegistrationController;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.CustomPrincipal;
import net.chikaboom.service.data.AccountDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Account> findAccount(@PathVariable int idAccount) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Object customPrincipal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Account resultAccount = accountOptional.get();

        if (customPrincipal.getClass() == String.class ||
                ((CustomPrincipal) customPrincipal).getIdAccount() != idAccount) {

            resultAccount.clearPersonalFields();
            resultAccount.getUserDetails().clearPersonalFields(resultAccount.getAccountSettings());
        }

        return ResponseEntity.ok(resultAccount);
    }

    /**
     * Производит поиск всех аккаунтов. Разрешено к использованию только администраторам
     *
     * @return возвращает все аккаунты
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Account>> findAllAccounts() {
        return new ResponseEntity<>(accountDataService.findAll(), HttpStatus.OK);
    }

    /**
     * Создает аккаунт. Разрешено к использованию только администраторам.
     * Для создания аккаунта см. {@link RegistrationController}.
     *
     * @param account создаваемый аккаунт
     * @return созданный аккаунта
     */
    @Deprecated
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountDataService.create(account));
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
    public ResponseEntity<Account> changeAccount(@PathVariable int idAccount, @RequestBody Account account) {
        Optional<Account> accountFromDb = accountDataService.findById(idAccount);

        if (!accountFromDb.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        account.setIdAccount(idAccount);
        Account patchedAccount;
        try {
            patchedAccount = accountDataService.patch(account);
        } catch (NumberParseException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(patchedAccount);
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
}
