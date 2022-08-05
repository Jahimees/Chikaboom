package net.chikaboom.controller.tab;

import net.chikaboom.model.ExpandableObject;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.tab.GeneralTabLoaderDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/personality/{idAccount}/settings/general")
public class GeneralSettingTabController {

    @Value("${tab.settings.general}")
    private String GENERAL_SETTINGS_TAB;
    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.phone}")
    private String PHONE;
    @Value("${attr.phoneCode}")
    private String PHONE_CODE;
    @Value("${attr.email}")
    private String EMAIL;
    private final ClientDataStorageService clientDataStorageService;
    private final GeneralTabLoaderDataService generalTabLoaderDataService;

    @Autowired
    public GeneralSettingTabController(ClientDataStorageService clientDataStorageService,
                                       GeneralTabLoaderDataService generalTabLoaderDataService) {
        this.clientDataStorageService = clientDataStorageService;
        this.generalTabLoaderDataService = generalTabLoaderDataService;
    }

    @GetMapping
    public String loadGeneralSettingTab(@PathVariable int idAccount, Model model) {
        clientDataStorageService.setData(ID_ACCOUNT, idAccount);

        ExpandableObject resultObject = generalTabLoaderDataService.execute();
        model.addAttribute(ID_ACCOUNT, resultObject.getField(ID_ACCOUNT));
        model.addAttribute(PHONE_CODE, resultObject.getField(PHONE_CODE));
        model.addAttribute(PHONE, resultObject.getField(PHONE));
        model.addAttribute(EMAIL, resultObject.getField(EMAIL));

        return GENERAL_SETTINGS_TAB;
    }

    @PostMapping
    public void updateGeneralSettingTab(@PathVariable int idAccount, Model model) {

    }
}
