package net.chikaboom.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Класс-контроллер. Перенаправляет на главную страницу
 */
@Controller
@RequestMapping("/chikaboom/main")
@PreAuthorize("permitAll()")
public class MainController {

    @Value("${page.main}")
    private String MAIN_PAGE;

    /**
     * Перенаправляет на главную страницу
     *
     * @return путь к главной странице
     */
    @PreAuthorize("permitAll()")
    @GetMapping
    public String openMainPage() {
        return MAIN_PAGE;
    }
}
