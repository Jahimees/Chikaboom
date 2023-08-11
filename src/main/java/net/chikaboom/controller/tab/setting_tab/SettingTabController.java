package net.chikaboom.controller.tab.setting_tab;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.chikaboom.service.data.AccountDataService;
import net.chikaboom.service.tab.EditSettingsTabService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Обрабатывает запросы, связанные с вкладкой настроек
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/chikaboom/personality/{idAccount}/settings")
public class SettingTabController {

    @Value("${tab.settings}")
    private String SETTINGS_TAB;
    @Value("${tab.settings.general}")
    private String GENERAL_SETTINGS_TAB;
    @Value("${tab.settings.profile}")
    private String PROFILE_SETTINGS_TAB;

    private final EditSettingsTabService editSettingsTabService;
    private final AccountDataService accountDataService;
    private final ObjectMapper objectMapper;

    private final Logger logger = Logger.getLogger(SettingTabController.class);

    /**
     * Открывает саму вкладку настроек пользователя.
     *
     * @param idAccount идентификатор аккаунта, чьи настройки необходимо открыть
     * @return ссылку на вкладку с настройками
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
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
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    @GetMapping("/general")
    public String loadGeneralSettingTab(@PathVariable int idAccount) {
        return GENERAL_SETTINGS_TAB;
    }

    /**
     * Возвращает представление настроек профиля (подвкладка вкладки настроек)
     *
     * @param idAccount идентификатор пользователя
     * @return ссылку на страницу
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    @GetMapping("/profile")
    public String loadProfileSettingTab(@PathVariable int idAccount) {
        return PROFILE_SETTINGS_TAB;
    }
}
