package net.chikaboom.controller;

import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.AuthorizationActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import static net.chikaboom.util.constant.RequestParametersConstant.*;

/**
 * Класс-контроллер, отвечающий за авторизацию пользователя.
 *
 * Контроллер сохраняет данные запроса в {@link ClientDataStorageService} и передает управление в {@link AuthorizationActionService}
 */
@Controller
@RequestMapping("/chikaboom/authorization")
public class AuthorizationController {

    private final ClientDataStorageService clientDataStorageService;
    private final AuthorizationActionService authorizationActionService;

    @Autowired
    public AuthorizationController(ClientDataStorageService clientDataStorageService, AuthorizationActionService authorizationActionService) {
        this.clientDataStorageService = clientDataStorageService;
        this.authorizationActionService = authorizationActionService;
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
        clientDataStorageService.setData(EMAIL, email);
        clientDataStorageService.setData(PASSWORD, password);
        clientDataStorageService.setData(SERVLET_REQUEST, request);

        return authorizationActionService.execute();
    }
}
