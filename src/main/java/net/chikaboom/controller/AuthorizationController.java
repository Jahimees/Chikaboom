package net.chikaboom.controller;

import net.chikaboom.service.action.AuthorizationService;
import net.chikaboom.service.ClientDataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Класс-контроллер, отвечающий за авторизацию пользователя.
 *
 * Контроллер сохраняет данные запроса в {@link ClientDataStorageService} и передает управление в {@link AuthorizationService}
 */
@Controller
@RequestMapping("/chikaboom/authorization")
public class AuthorizationController {

    ClientDataStorageService clientDataStorageService;
    AuthorizationService authorizationService;

    @Autowired
    public AuthorizationController(ClientDataStorageService clientDataStorageService, AuthorizationService authorizationService) {
        this.clientDataStorageService = clientDataStorageService;
        this.authorizationService = authorizationService;
    }

    /**
     * Сохранение данных и передача управления в сервис
     *
     * @param email параметр электронной почты
     * @param password параметр пароля
     * @param request запрос для использования сессии
     * @return название страницы, на которую будет переведен пользователь
     */
    @GetMapping
    public String authorize(@RequestParam String email, @RequestParam String password, HttpServletRequest request) {
        clientDataStorageService.setData("email", email);
        clientDataStorageService.setData("password", password);
        clientDataStorageService.setData("servletRequest", request);

        return authorizationService.execute();
    }
}
