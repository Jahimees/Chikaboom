package net.chikaboom.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Класс-контроллер. Перенаправляет на главную страницу
 */
@Controller
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/main")
public class MainController {

    @Value("${page.main}")
    private String MAIN_PAGE;

    @GetMapping
    public String openMainPage() {
        return MAIN_PAGE;
    }
}
