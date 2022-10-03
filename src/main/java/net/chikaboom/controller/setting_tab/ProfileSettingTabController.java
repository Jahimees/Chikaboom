package net.chikaboom.controller.setting_tab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/personality/{idAccount}/settings/profile")
public class ProfileSettingTabController {

    @Value("${tab.settings.profile}")
    private String PROFILE_SETTINGS_TAB;

    @GetMapping
    public String loadProfileSettingTab(@PathVariable int idAccount) {
        return PROFILE_SETTINGS_TAB;
    }

}
