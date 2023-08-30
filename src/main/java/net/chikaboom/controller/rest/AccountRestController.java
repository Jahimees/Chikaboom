package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.controller.RegistrationController;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.UserDetails;
import net.chikaboom.service.data.AccountDataService;
import net.chikaboom.service.data.UserDetailsDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final UserDetailsDataService userDetailsDataService;

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

        return accountOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @GetMapping("/{idAccount}/clients")
    public ResponseEntity<List<UserDetails>> findClients(@PathVariable int idAccount) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userDetailsDataService.findClientsWithExtraInfo(idAccount));
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
     * Производит изменение аккаунта (полная замена).
     * Необходимо быть авторизованным. Невозможно изменять чужой аккаунт.
     *
     * @param idAccount идентификатор изменяемого аккаунта
     * @param account   новая версия аккаунта
     * @return измененный аккаунт
     */
    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @PutMapping("/{idAccount}")
    public ResponseEntity<Account> replaceAccount(@PathVariable int idAccount, @RequestBody Account account) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        account.setIdAccount(idAccount);

        return ResponseEntity.ok(accountDataService.update(account));
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
        return ResponseEntity.ok(accountDataService.patch(account));
    }

    //    TODO NEW LOGOUT!!!!

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
