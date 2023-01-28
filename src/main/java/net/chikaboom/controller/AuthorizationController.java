package net.chikaboom.controller;

import net.chikaboom.controller.error.AdviceController;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.AuthorizationActionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Класс-контроллер, отвечающий за авторизацию пользователя.
 * <p>
 * Контроллер сохраняет данные запроса в {@link ClientDataStorageService} и передает управление в {@link AuthorizationActionService}
 */
@Controller
@RequestMapping("/chikaboom/authorization")
public class AuthorizationController {

    @Value("${attr.servletRequest}")
    private String SERVLET_REQUEST;
    @Value("${attr.password}")
    private String PASSWORD;
    @Value("${attr.phone}")
    private String PHONE;
    @Value("${attr.phoneCode}")
    private String PHONE_CODE;

    private final ClientDataStorageService clientDataStorageService;
    private final AuthorizationActionService authorizationActionService;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public AuthorizationController(ClientDataStorageService clientDataStorageService, AuthorizationActionService authorizationActionService) {
        this.clientDataStorageService = clientDataStorageService;
        this.authorizationActionService = authorizationActionService;
    }

    /**
     * Сохраняет параметры с клиента и передает управление в сервис авторизации {@link AuthorizationActionService}
     *
     * @param phoneCode параметр кода страны
     * @param phone     параметр номера телефона пользователя
     * @param password  параметр пароля
     * @param request   запрос для использования сессии
     * @return объект-ответ, содержащий название страницы, на которую должен будет осуществлен переход и http статус.
     * В случае ошибки возвращает объект-ответ-ошибки с помощью {@link AdviceController}
     */
    @GetMapping
    public ResponseEntity<?> authorize(@RequestParam String phoneCode, @RequestParam String phone,
                                       @RequestParam String password, HttpServletRequest request) {
        logger.info("Authorizing of user with phone +" + phoneCode + " " + phoneCode);
        clientDataStorageService.setData(PHONE_CODE, phoneCode);
        clientDataStorageService.setData(PHONE, phone);
        clientDataStorageService.setData(PASSWORD, password);
        clientDataStorageService.setData(SERVLET_REQUEST, request);

        return new ResponseEntity<>("/chikaboom/" + authorizationActionService.executeAndGetPage(), HttpStatus.OK);
    }
}
