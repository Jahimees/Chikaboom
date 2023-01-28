package net.chikaboom.controller;

import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.LogoutActionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Осуществляет выход из аккаунта и сброс сессии
 */
@Controller
@RequestMapping("/chikaboom/logout")
public class LogoutController {

    @Value("${attr.servletRequest}")
    private String SERVLET_REQUEST;

    private final ClientDataStorageService clientDataStorageService;
    private final LogoutActionService logoutActionService;

    @Autowired
    public LogoutController(ClientDataStorageService clientDataStorageService, LogoutActionService logoutActionService) {
        this.clientDataStorageService = clientDataStorageService;
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
        clientDataStorageService.setData(SERVLET_REQUEST, request);

        return "redirect:" + logoutActionService.executeAndGetPage();
    }
}
