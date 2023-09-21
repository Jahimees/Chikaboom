package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.AccountSettings;
import net.chikaboom.service.data.AccountSettingsDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AccountSettingsRestController {

    private final AccountSettingsDataService accountSettingsDataService;

    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @GetMapping("/accounts/{idAccount}/settings")
    public ResponseEntity<AccountSettings> getAccountSettingsByIdAccount(@PathVariable int idAccount) {
        Optional<AccountSettings> accountSettingsOptional = accountSettingsDataService.findByIdAccount(idAccount);

        if (!accountSettingsOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(accountSettingsOptional.get());
    }

    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @PatchMapping("/accounts/{idAccount}/settings")
    public ResponseEntity<AccountSettings> patchAccountSettings(@PathVariable int idAccount,
                                                                @RequestBody AccountSettings accountSettings) {

        return ResponseEntity.ok(accountSettingsDataService.patch(idAccount, accountSettings));
    }

}
