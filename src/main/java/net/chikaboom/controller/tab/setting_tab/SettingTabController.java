package net.chikaboom.controller.tab.setting_tab;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.model.database.Account;
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
 * Обрабатывает запросы, связанные с вкладкой настроек
 */
@Controller
@RequestMapping("/chikaboom/personality/{idAccount}/settings")
public class SettingTabController {
//    TODO NEW idAccount проверки?

    @Value("${tab.settings}")
    private String SETTINGS_TAB;
    @Value("${tab.settings.general}")
    private String GENERAL_SETTINGS_TAB;
    @Value("${tab.settings.profile}")
    private String PROFILE_SETTINGS_TAB;

    private final EditSettingsTabService editSettingsTabService;
    private final AccountInfoLoaderService accountInfoLoaderService;

    private final Logger logger = Logger.getLogger(SettingTabController.class);

    @Autowired
    public SettingTabController(EditSettingsTabService editSettingsTabService,
                                AccountInfoLoaderService accountInfoLoaderService) {
        this.editSettingsTabService = editSettingsTabService;
        this.accountInfoLoaderService = accountInfoLoaderService;
    }

    /**
     * Открывает саму вкладку настроек пользователя.
     *
     * @param idAccount идентификатор аккаунта, чьи настройки необходимо открыть
     * @return ссылку на вкладку с настройками
     */
    @GetMapping
    public String openSettingTab(@PathVariable int idAccount) {
        return SETTINGS_TAB;
    }

    /**
     * Возвращает представление основных настроек (подвкладка вкладки настроек).
     *
     * @param idAccount идентификатор пользователя
     * @return ссылку на страницу
     */
    @GetMapping("/general")
    public String loadGeneralSettingTab(@PathVariable int idAccount) {
        return GENERAL_SETTINGS_TAB;
    }

    /**
     * Возвращает представление настроек профиля (подвкладка вкладки настрое)
     *
     * @param idAccount идентификатор пользователя
     * @return ссылку на страницу
     */
    @GetMapping("/profile")
    public String loadProfileSettingTab(@PathVariable int idAccount) {
        return PROFILE_SETTINGS_TAB;
    }

    /**
     * Изменяет выбранное(ые) поле(я) аккаунта, после чего перезагружает данные на представлении.
     *
     * @param idAccount      идентификатор изменяемого аккаунта
     * @param changedAccount новый измененный аккаунт в виде пар ключ-значение
     * @return сохраненный объект в виде json
     */
//        TODO NEW Исправить. Сделать PATCH запрос. Туториал на офф сайте спринга
    @PutMapping
    public ResponseEntity<String> updateSettingTab(@PathVariable int idAccount,
                                                   @RequestBody Map<String, Object> changedAccount) {
        logger.info("Editing user data. idUser: " + idAccount);
        logger.info("New data: " + changedAccount);

        editSettingsTabService.updateAccountSettings(changedAccount);

        logger.info("Reloading personality page.");
        Account account = accountInfoLoaderService.findAccountById(idAccount);
        ObjectMapper mapper = new ObjectMapper();

        String accountJSON = null;
        try {
            accountJSON = mapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        return new ResponseEntity<>(accountJSON, HttpStatus.OK);
    }
}
