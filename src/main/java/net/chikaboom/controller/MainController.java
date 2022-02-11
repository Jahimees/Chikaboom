package net.chikaboom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static net.chikaboom.util.constant.PageConstant.MAIN_PAGE;

/**
 * Класс-контроллер. Перенаправляет на главную страницу
 */
@Controller
@RequestMapping("/chikaboom")
public class MainController {

    @GetMapping
    public String processRequest() {
        return MAIN_PAGE;
    }
}
