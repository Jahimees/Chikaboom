package net.chikaboom.controller;

import net.chikaboom.annotation.LoggableViewController;
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

    /**
     * Перенаправляет на страницу аккаунта
     *
     * @return путь к странице аккаунта
     */
    @PreAuthorize("permitAll()")
    @GetMapping
    @LoggableViewController
    public String openAccountPage(@PathVariable int idAccount) {
        return ACCOUNT_PAGE;
    }
}
