package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.AccountSettingsFacade;
import net.chikaboom.model.database.AccountSettings;
import net.chikaboom.service.data.AccountSettingsDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST контроллер для взаимодействия с сущностями типа {@link AccountSettings}
 */
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
    public ResponseEntity<AccountSettingsFacade> findAccountSettingsByIdAccount(@PathVariable int idAccount) {
        AccountSettingsFacade accountSettings = accountSettingsDataService.findByIdAccount(idAccount);

        return ResponseEntity.ok(accountSettings);
    }

    /**
     * Производит частичное изменение настроек профиля в зависимости от параметров, которые присутсвуют в запросе
     *
     * @param idAccount       идентификатор аккаунта
     * @param accountSettingsFacade объект с новыми данными, которые должны быть сохранены в базу данных
     * @return обновленный объект настроек
     */
    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @PatchMapping("/accounts/{idAccount}/settings")
    public ResponseEntity<AccountSettingsFacade> patchAccountSettings(@PathVariable int idAccount,
                                                                @RequestBody AccountSettingsFacade accountSettingsFacade) {

        return ResponseEntity.ok(accountSettingsDataService.patch(idAccount, accountSettingsFacade));
    }
}
