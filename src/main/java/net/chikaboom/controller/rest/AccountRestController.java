package net.chikaboom.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.service.data.AccountDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountRestController {

    private final AccountDataService accountDataService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{idAccount}")
    public ResponseEntity<Account> findAccount(@PathVariable int idAccount) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        return accountOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping
    public ResponseEntity<List<Account>> findAllAccounts() {
        return new ResponseEntity<>(accountDataService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountDataService.create(account));
    }

    @PutMapping("/{idAccount}")
    public ResponseEntity<Account> replaceAccount(@PathVariable int idAccount, @RequestBody Account account) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(accountDataService.update(account));
    }

    @PatchMapping(path = "/{idAccount}", consumes = "application/json-patch+json")
    public ResponseEntity<Account> changeAccount(@PathVariable int idAccount, @RequestBody JsonPatch jsonPatch) {
        try {
            Account account = accountDataService.findById(idAccount)
                    .orElseThrow(() -> new NoSuchDataException("Account with id " + idAccount + " not found."));
            Account patchedAccount = applyPatchToAccount(jsonPatch, account);

            return ResponseEntity.ok(accountDataService.update(patchedAccount));
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.internalServerError().build();
        } catch (NoSuchDataException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{idAccount}")
    public void deleteAccount(@PathVariable int idAccount) {
        accountDataService.deleteById(idAccount);
    }

    private Account applyPatchToAccount(JsonPatch jsonPatch, Account targetAccount)
            throws JsonProcessingException, JsonPatchException {
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(targetAccount, JsonNode.class));
        return objectMapper.treeToValue(patched, Account.class);
    }
}
