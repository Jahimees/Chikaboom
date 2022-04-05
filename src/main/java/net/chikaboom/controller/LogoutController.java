package net.chikaboom.controller;

import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.LogoutActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static net.chikaboom.util.constant.RequestParametersConstant.SERVLET_REQUEST;

@Controller
@RequestMapping("/chikaboom/logout")
public class LogoutController {

    private final ClientDataStorageService clientDataStorageService;
    private final LogoutActionService logoutActionService;

    @Autowired
    public LogoutController(ClientDataStorageService clientDataStorageService, LogoutActionService logoutActionService) {
        this.clientDataStorageService = clientDataStorageService;
        this.logoutActionService = logoutActionService;
    }

    @GetMapping
    public String logout(HttpServletRequest request) {
        clientDataStorageService.setData(SERVLET_REQUEST, request);

        return "redirect:" + logoutActionService.execute();
    }
}
