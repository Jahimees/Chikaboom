package net.chikaboom.controller.tab.setting_tab;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.model.database.Account;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.AccountInfoLoaderService;
import net.chikaboom.service.action.tab.EditSettingsTabService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Перехватывает события отвечающие за вкладку настроек
 */
@Controller
@RequestMapping("/chikaboom/personality/{idAccount}/settings")
public class SettingTabController {

    @Value("${tab.settings}")
    private String SETTINGS_TAB;
    @Value("${tab.settings.general}")
    private String GENERAL_SETTINGS_TAB;
    @Value("${tab.settings.profile}")
    private String PROFILE_SETTINGS_TAB;
    @Value("${attr.customAccount}")
    private String CUSTOM_ACCOUNT;
    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;

    private final ClientDataStorageService clientDataStorageService;
    private final EditSettingsTabService editSettingsTabService;
    private final AccountInfoLoaderService accountInfoLoaderService;

    private final Logger logger = Logger.getLogger(SettingTabController.class);

    @Autowired
    public SettingTabController(ClientDataStorageService clientDataStorageService,
                                EditSettingsTabService editSettingsTabService,
                                AccountInfoLoaderService accountInfoLoaderService) {
        this.clientDataStorageService = clientDataStorageService;
        this.editSettingsTabService = editSettingsTabService;
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
     * Возвращает представление основных настроек
     *
     * @param idAccount идентификатор пользователя
     * @return ссылку на страницу
     */
    @GetMapping("/general")
    public String loadGeneralSettingTab(@PathVariable int idAccount) {
        return GENERAL_SETTINGS_TAB;
    }

    /**
     * Возвращает представление настроек профиля
     *
     * @param idAccount идентификатор пользователя
     * @return ссылку на страницу
     */
    @GetMapping("/profile")
    public String loadProfileSettingTab(@PathVariable int idAccount) {
        return PROFILE_SETTINGS_TAB;
    }

    /**
     * Изменяет выбранное(ые) поле(я) аккаунта
     *
     * @param idAccount      идентификатор изменяемого аккаунта
     * @param changedAccount новый измененный аккаунт в виде пар ключ-значение
     * @return сохраненный объект в виде json
     */
    @PostMapping
    public ResponseEntity<String> updateSettingTab(@PathVariable int idAccount,
                                                   @RequestBody Map<String, Object> changedAccount) {
        logger.info("Editing user data. idUser: " + idAccount);
        logger.info(changedAccount);

        clientDataStorageService.setData(CUSTOM_ACCOUNT, changedAccount);
        clientDataStorageService.setData(ID_ACCOUNT, idAccount);

        editSettingsTabService.executeAndGetOne();
        logger.info("Data was successfully change");

        logger.info("Reloading personality page...");
        Account account = accountInfoLoaderService.executeAndGetOne();
        ObjectMapper mapper = new ObjectMapper();

        String accountJSON = null;
        try {
            accountJSON = mapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            //            TODO EXCEPTION
            e.printStackTrace();
        }

        clientDataStorageService.clearAllData();

        return new ResponseEntity<>(accountJSON, HttpStatus.OK);
    }
}
