package net.chikaboom.controller.tab;

import net.chikaboom.model.database.Account;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.tab.EditGeneralSettingsService;
import net.chikaboom.service.action.tab.GeneralTabLoaderDataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер загрузки вкладки "Основные настройки"
 */
@Controller
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/personality/{idAccount}/settings/general")
public class GeneralSettingTabController {

    @Value("${tab.settings.general}")
    private String GENERAL_SETTINGS_TAB;
    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.account}")
    private String ACCOUNT;
    @Value("${attr.phone}")
    private String PHONE;
    @Value("${attr.phoneCode}")
    private String PHONE_CODE;
    @Value("${attr.email}")
    private String EMAIL;

    private final ClientDataStorageService clientDataStorageService;
    private final GeneralTabLoaderDataService generalTabLoaderDataService;
    private final EditGeneralSettingsService editGeneralSettingsService;

    private final Logger logger = Logger.getLogger(GeneralSettingTabController.class);

    @Autowired
    public GeneralSettingTabController(ClientDataStorageService clientDataStorageService,
                                       GeneralTabLoaderDataService generalTabLoaderDataService, EditGeneralSettingsService editGeneralSettingsService) {
        this.clientDataStorageService = clientDataStorageService;
        this.generalTabLoaderDataService = generalTabLoaderDataService;
        this.editGeneralSettingsService = editGeneralSettingsService;
    }

    /**
     * Передаёт основные параметры пользователя для отображения их на клиенте
     *
     * @param idAccount идентификатор пользователя
     * @param model     модель, в которой хранятся все необходимые данные для передачи
     * @return ссылку на страницу
     */
    @GetMapping
    public String loadGeneralSettingTab(@PathVariable int idAccount, Model model) {
        clientDataStorageService.setData(ID_ACCOUNT, idAccount);

        Map<String, Object> accountParameters = generalTabLoaderDataService.execute();
        model.addAttribute(ID_ACCOUNT, accountParameters.get(ID_ACCOUNT));
        model.addAttribute(PHONE_CODE, accountParameters.get(PHONE_CODE));
        model.addAttribute(PHONE, accountParameters.get(PHONE));
        model.addAttribute(EMAIL, accountParameters.get(EMAIL));

        return GENERAL_SETTINGS_TAB;
    }

    /**
     * Изменяет определенное поле аккаунта
     *
     * @param idAccount      идентификатор изменяемого аккаунта
     * @param changedAccount новый измененный аккаунт в виде пар ключ-значение
     * @return сохраненный объект в виде json
     */
    @PostMapping
    public ResponseEntity<Account> updateGeneralSettingTab(@PathVariable int idAccount,
                                                           @RequestBody Map<String, Object> changedAccount) {
        logger.info("Editing user data. idUser: " + idAccount);
        logger.info(changedAccount);

        clientDataStorageService.setData(ACCOUNT, changedAccount);
        clientDataStorageService.setData(ID_ACCOUNT, idAccount);

        Account account = editGeneralSettingsService.execute();

        logger.info("Data was successfully change");

        clientDataStorageService.dropData(ACCOUNT);
        clientDataStorageService.dropData(ID_ACCOUNT);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }
}
