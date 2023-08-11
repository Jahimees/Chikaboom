package net.chikaboom.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.service.data.AccountDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//TODO проверки на совпадение id и PreAuthorize
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountRestController {

    private final AccountDataService accountDataService;
    private final ObjectMapper objectMapper;

    @PreAuthorize("permitAll()")
    @GetMapping("/{idAccount}")
    public ResponseEntity<Account> findAccount(@PathVariable int idAccount) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        return accountOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<Account>> findAllAccounts() {
        return new ResponseEntity<>(accountDataService.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountDataService.create(account));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{idAccount}")
    public ResponseEntity<Account> replaceAccount(@PathVariable int idAccount, @RequestBody Account account) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Account authorizedAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authorizedAccount.getIdAccount() != idAccount) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        account.setIdAccount(idAccount);

        return ResponseEntity.ok(accountDataService.update(account));
    }

    /**
     * Изменяет выбранное(ые) поле(я) аккаунта, после чего перезагружает данные на представлении.
     *
     * @param idAccount идентификатор изменяемого аккаунта
     * @return сохраненный объект в виде json
     */
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{idAccount}")
    public ResponseEntity<Account> changeAccount(@PathVariable int idAccount, @RequestBody Account account) {
        Optional<Account> accountFromDb = accountDataService.findById(idAccount);

        if (!accountFromDb.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Account authorizedAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authorizedAccount.getIdAccount() != accountFromDb.get().getIdAccount()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        account.setIdAccount(idAccount);
        return ResponseEntity.ok(accountDataService.patch(account));
    }

//    TODO NEW LOGOUT!!!!
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{idAccount}")
    public ResponseEntity<String> deleteAccount(@PathVariable int idAccount) {

        Account authorizedAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authorizedAccount.getIdAccount() != idAccount) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        accountDataService.deleteById(idAccount);
        return ResponseEntity.ok().build();
    }
}
