package net.chikaboom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.model.database.Account;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.AccountInfoLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Перенаправляет на страницу аккаунта
 */
@RestController
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/account/{idAccount}")
public class AccountController {

    @Value("${page.account}")
    private String ACCOUNT_PAGE;
    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.account}")
    private String ACCOUNT;

    private final ClientDataStorageService clientDataStorageService;
    private final AccountInfoLoaderService accountInfoLoaderService;

    @Autowired
    public AccountController(ClientDataStorageService clientDataStorageService, AccountInfoLoaderService accountInfoLoaderService) {
        this.clientDataStorageService = clientDataStorageService;
        this.accountInfoLoaderService = accountInfoLoaderService;
    }

    /**
     * Перенаправляет на страницу аккаунта
     *
     * @return ссылку на страницу аккаунта
     */
    @GetMapping
    public ModelAndView openAccountPage(@PathVariable int idAccount) {
        ModelAndView modelAndView = new ModelAndView(ACCOUNT_PAGE);
        clientDataStorageService.setData(ID_ACCOUNT, idAccount);

        Account account = accountInfoLoaderService.executeAndGetOne();
        ObjectMapper mapper = new ObjectMapper();

        String result = "";
        try {
            result = mapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
//            TODO EXCEPTION
            e.printStackTrace();
        }
        modelAndView.addObject(ACCOUNT, result);

        return modelAndView;
    }
}
