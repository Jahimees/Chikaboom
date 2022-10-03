package net.chikaboom.controller.setting_tab;

import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.LoadPersonalityService;
import net.chikaboom.service.action.tab.EditGeneralSettingsService;
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
    @Value("${attr.account}")
    private String ACCOUNT;
    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;

    private final ClientDataStorageService clientDataStorageService;
    private final EditGeneralSettingsService editGeneralSettingsService;
    private final LoadPersonalityService loadPersonalityService;

    private final Logger logger = Logger.getLogger(SettingTabController.class);

    @Autowired
    public SettingTabController(ClientDataStorageService clientDataStorageService,
                                EditGeneralSettingsService editGeneralSettingsService,
                                LoadPersonalityService loadPersonalityService) {
        this.clientDataStorageService = clientDataStorageService;
        this.editGeneralSettingsService = editGeneralSettingsService;
        this.loadPersonalityService = loadPersonalityService;
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
     * Изменяет определенное поле аккаунта
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

        clientDataStorageService.setData(ACCOUNT, changedAccount);
        clientDataStorageService.setData(ID_ACCOUNT, idAccount);

        editGeneralSettingsService.execute();
        logger.info("Data was successfully change");

        logger.info("Reloading personality page...");
        Map<String, Object> parameters = loadPersonalityService.execute();

        clientDataStorageService.dropData(ACCOUNT);
        clientDataStorageService.dropData(ID_ACCOUNT);

        return new ResponseEntity<>(parameters, HttpStatus.OK);
    }
}
