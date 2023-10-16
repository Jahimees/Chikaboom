package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.AccountSettingsFacadeConverter;
import net.chikaboom.facade.dto.AccountSettingsFacade;
import net.chikaboom.facade.dto.Facade;
import net.chikaboom.model.database.AccountSettings;
import net.chikaboom.model.response.CustomResponseObject;
import net.chikaboom.service.data.AccountSettingsDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public ResponseEntity<Facade> findAccountSettingsByIdAccount(@PathVariable int idAccount) {
        Optional<AccountSettings> accountSettingsOptional = accountSettingsDataService.findByIdAccount(idAccount);

        if (!accountSettingsOptional.isPresent()) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.NOT_FOUND.value(),
                    "There not found account settings with idAccount " + idAccount,
                    "GET:/accounts/" + idAccount + "/settings"
            ), HttpStatus.NOT_FOUND);
        }

        AccountSettingsFacade accountSettingsFacade =
                AccountSettingsFacadeConverter.convertToDto(accountSettingsOptional.get());

        return ResponseEntity.ok(accountSettingsFacade);
    }

    /**
     * Производит частичное изменение настроек профиля в зависимости от параметров, которые присутсвуют в запросе
     *
     * @param idAccount             идентификатор аккаунта
     * @param accountSettingsFacade объект с новыми данными, которые должны быть сохранены в базу данных
     * @return обновленный объект настроек
     */
    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @PatchMapping("/accounts/{idAccount}/settings")
    public ResponseEntity<AccountSettingsFacade> patchAccountSettings(@PathVariable int idAccount,
                                                                      @RequestBody AccountSettingsFacade accountSettingsFacade) {
        AccountSettings accountSettings = AccountSettingsFacadeConverter.convertToModel(accountSettingsFacade);

        AccountSettings patchedAccountSettings = accountSettingsDataService.patch(idAccount, accountSettings);

        return ResponseEntity.ok(AccountSettingsFacadeConverter.convertToDto(patchedAccountSettings));
    }
}
