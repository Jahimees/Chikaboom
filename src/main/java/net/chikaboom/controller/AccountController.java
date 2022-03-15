package net.chikaboom.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chikaboom/account")
public class AccountController {

    @Value("${page.account}")
    private String ACCOUNT_PAGE;

    @GetMapping
    public String processRequest() {
        return ACCOUNT_PAGE;
    }
}
