package net.chikaboom.controller.setting_tab;

import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.AccountInfoLoaderService;
import net.chikaboom.service.action.tab.EditSettingsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Загружает вкладку настроек
 */
@Controller
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/personality/{idAccount}/settings")
public class SettingTabController {

    @Value("${tab.settings}")
    private String SETTINGS_TAB;
    @Value("${attr.customAccount}")
    private String CUSTOM_ACCOUNT;
    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;

    private final ClientDataStorageService clientDataStorageService;
    private final EditSettingsService editSettingsService;
    private final AccountInfoLoaderService accountInfoLoaderService;

    private final Logger logger = Logger.getLogger(SettingTabController.class);

    @Autowired
    public SettingTabController(ClientDataStorageService clientDataStorageService,
                                EditSettingsService editSettingsService,
                                AccountInfoLoaderService accountInfoLoaderService) {
        this.clientDataStorageService = clientDataStorageService;
        this.editSettingsService = editSettingsService;
        this.accountInfoLoaderService = accountInfoLoaderService;
    }

    /**
     * Открывает вкладку настроек
     *
     * @param idAccount идентификатор аккаунта
     * @return ссылку на вкладку с настройками
     */
    @GetMapping
    public String openSettingTab(@PathVariable int idAccount) {
        return SETTINGS_TAB;
    }

    /**
     * Изменяет выбранное(ые) поле(я) аккаунта
     *
     * @param idAccount      идентификатор изменяемого аккаунта
     * @param changedAccount новый измененный аккаунт в виде пар ключ-значение
     * @return сохраненный объект в виде json
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> updateSettingTab(@PathVariable int idAccount,
                                                                @RequestBody Map<String, Object> changedAccount) {
        logger.info("Editing user data. idUser: " + idAccount);
        logger.info(changedAccount);

        clientDataStorageService.setData(CUSTOM_ACCOUNT, changedAccount);
        clientDataStorageService.setData(ID_ACCOUNT, idAccount);

        editSettingsService.execute();
        logger.info("Data was successfully change");

        logger.info("Reloading personality page...");
        Map<String, Object> parameters = accountInfoLoaderService.execute();

        clientDataStorageService.clearAllData();

        return new ResponseEntity<>(parameters, HttpStatus.OK);
    }
}
