package net.chikaboom.controller;

import net.chikaboom.controller.error.AdviceController;
import net.chikaboom.service.action.AuthorizationActionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Класс-контроллер, отвечающий за авторизацию пользователя.
 * <p>
 * Контроллер и передает управление в {@link AuthorizationActionService}
 */
@Controller
@RequestMapping("/chikaboom/login")
public class AuthorizationController {

    private final AuthorizationActionService authorizationActionService;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public AuthorizationController(AuthorizationActionService authorizationActionService) {
        this.authorizationActionService = authorizationActionService;
    }

    /**
     * Передает управление в сервис авторизации {@link AuthorizationActionService}
     * <p>
     * //     * @param phoneCode параметр кода страны
     * //     * @param phone     параметр номера телефона пользователя
     *
     * @param password параметр пароля
     *                 //     * @param request   запрос для использования сессии
     * @return объект-ответ, содержащий название страницы, на которую должен будет осуществлен переход и http статус.
     * В случае ошибки возвращает объект-ответ-ошибки с помощью {@link AdviceController}
     */
    @PostMapping
    public ResponseEntity<?> authorize(@RequestParam String nickname, @RequestParam String password) {
        logger.info("Authorizing of user with name " + nickname);

//        TODO FIXME NEW странный путь в ResponseEntity и странный return
        return new ResponseEntity<>("/chikaboom/" + authorizationActionService.authorize(nickname, password), HttpStatus.OK);
    }
}
