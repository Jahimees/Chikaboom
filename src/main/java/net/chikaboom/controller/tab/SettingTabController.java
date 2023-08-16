package net.chikaboom.controller.tab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Отвечает за отрисовку вкладки настроек
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

    /**
     * Открывает саму вкладку настроек пользователя.
     *
     * @param idAccount идентификатор аккаунта, чьи настройки необходимо открыть
     * @return путь к вкладке с настройками
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    @GetMapping
    public String openSettingTab(@PathVariable int idAccount) {
        return SETTINGS_TAB;
    }

    /**
     * Возвращает представление основных настроек (подвкладка настроек).
     *
     * @param idAccount идентификатор пользователя
     * @return путь к подвкладке основных настроек
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
     * @return путь к подвкладке настроек профиля
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    @GetMapping("/profile")
    public String loadProfileSettingTab(@PathVariable int idAccount) {
        return PROFILE_SETTINGS_TAB;
    }
}
