package net.chikaboom.controller.tab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Загружает вкладку настроек
 */
@Controller
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/personality/{idAccount}/settings")
public class SettingTabController {

    @Value("${tab.settings}")
    private String SETTINGS_TAB;

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
}
