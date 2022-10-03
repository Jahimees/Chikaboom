package net.chikaboom.controller.setting_tab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер загрузки вкладки "Основные настройки"
 */
@Controller
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/personality/{idAccount}/settings/general")
public class GeneralSettingTabController {

    @Value("${tab.settings.general}")
    private String GENERAL_SETTINGS_TAB;

    /**
     * Возвращает представление основных настроек
     *
     * @param idAccount идентификатор пользователя
     * @return ссылку на страницу
     */
    @GetMapping
    public String loadGeneralSettingTab(@PathVariable int idAccount) {
        return GENERAL_SETTINGS_TAB;
    }

}
