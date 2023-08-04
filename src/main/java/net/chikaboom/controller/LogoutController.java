package net.chikaboom.controller;

import jakarta.servlet.http.HttpServletRequest;
import net.chikaboom.service.action.LogoutActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Осуществляет выход из аккаунта и сброс сессии
 */
@Controller
@RequestMapping("/chikaboom/logout")
public class LogoutController {

    private final LogoutActionService logoutActionService;

    @Autowired
    public LogoutController(LogoutActionService logoutActionService) {
        this.logoutActionService = logoutActionService;
    }

    /**
     * Передает управление сервису и перенаправляет на главную страницу
     *
     * @param request объект запроса
     * @return команду перенаправления на страницу
     */
    @GetMapping
    public String logout(HttpServletRequest request) {
        return "redirect:" + logoutActionService.logout(request);
    }
}
