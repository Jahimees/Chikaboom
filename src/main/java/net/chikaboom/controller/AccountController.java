package net.chikaboom.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/account")
public class AccountController {

    @Value("${page.account}")
    private String ACCOUNT_PAGE;

    @GetMapping
    public String openAccountPage() {
        return ACCOUNT_PAGE;
    }
}