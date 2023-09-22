package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.AccountSettings;
import net.chikaboom.service.data.AccountSettingsDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST контроллер для взаимодействия с сущностями типа {@link AccountSettings}
 */
//TODO нет документации в README файле
@RestController
@RequiredArgsConstructor
public class AccountSettingsRestController {

    private final AccountSettingsDataService accountSettingsDataService;

    /**
     * Производит поиск настроек аккаунта по идентификатору аккаунта
     *
     * @param idAccount идентификатор аккаунта
     * @return найденные настройки в формате json
     */
    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @GetMapping("/accounts/{idAccount}/settings")
    public ResponseEntity<AccountSettings> getAccountSettingsByIdAccount(@PathVariable int idAccount) {
        Optional<AccountSettings> accountSettingsOptional = accountSettingsDataService.findByIdAccount(idAccount);

        if (!accountSettingsOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(accountSettingsOptional.get());
    }

    /**
     * Производит частичное изменение настроек профиля в зависимости от параметров, которые присутсвуют в запросе
     *
     * @param idAccount       идентификатор аккаунта
     * @param accountSettings объект с новыми данными, которые должны быть сохранены в базу данных
     * @return обновленный объект настроек
     */
    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @PatchMapping("/accounts/{idAccount}/settings")
    public ResponseEntity<AccountSettings> patchAccountSettings(@PathVariable int idAccount,
                                                                @RequestBody AccountSettings accountSettings) {

        return ResponseEntity.ok(accountSettingsDataService.patch(idAccount, accountSettings));
    }

}
