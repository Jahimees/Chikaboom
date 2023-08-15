package net.chikaboom.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер отвечает за отрисовку страницы аккаунта
 */
@Controller
@RequestMapping("/chikaboom/account/{idAccount}")
public class AccountController {

    @Value("${page.account}")
    private String ACCOUNT_PAGE;

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Перенаправляет на страницу аккаунта
     *
     * @return путь к странице аккаунта
     */
    @PreAuthorize("permitAll()")
    @GetMapping
    public String openAccountPage(@PathVariable int idAccount) {
        logger.info("Loading account page for account with id " + idAccount);

        return ACCOUNT_PAGE;
    }
}
