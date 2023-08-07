package net.chikaboom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.model.database.Account;
import net.chikaboom.service.action.AccountInfoLoaderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Обработка запросов на странице аккаунта
 */
@RestController
@RequestMapping("/chikaboom/account/{idAccount}")
//@PreAuthorize("permitAll()")
public class AccountController {

    @Value("${page.account}")
    private String ACCOUNT_PAGE;
    @Value("${attr.account}")
    private String ACCOUNT;

    private final AccountInfoLoaderService accountInfoLoaderService;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public AccountController(AccountInfoLoaderService accountInfoLoaderService) {
        this.accountInfoLoaderService = accountInfoLoaderService;
    }

    /**
     * Перенаправляет на страницу аккаунта
     *
     * @return путь к странице аккаунта
     */
    @GetMapping
    public ModelAndView openAccountPage(@PathVariable int idAccount) {
        logger.info("Loading account page for account with id " + idAccount);
        ModelAndView modelAndView = new ModelAndView(ACCOUNT_PAGE);

        Account account = accountInfoLoaderService.findAccountById(idAccount);
        ObjectMapper mapper = new ObjectMapper();

        String result = "";
        try {
            logger.info("Trying to convert account to JSON form.");
            result = mapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        modelAndView.addObject(ACCOUNT, result);

        return modelAndView;
    }
}
