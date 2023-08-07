package net.chikaboom.controller.auth;

import net.chikaboom.controller.error.AdviceController;
import net.chikaboom.service.action.RegistrationActionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Класс-контроллер, отвечающий за регистрацию пользователя.
 * <p>
 * Контроллер передает управление в {@link RegistrationActionService}
 */
@Controller
@RequestMapping("/chikaboom/registration")
public class RegistrationController {

    private final RegistrationActionService registrationActionService;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public RegistrationController(RegistrationActionService registrationActionService) {
        this.registrationActionService = registrationActionService;
    }

    /**
     * Сохраняет параметры клиента и передает управление в сервис {@link RegistrationActionService}
     *
     * @param phoneCode параметр кода страны
     * @param phone     параметр номера телефона пользователя
     * @param password  параметр пароля
     * @param nickname  имя регистрируемого пользователя
     * @param role      тип регистрируемого аккаунта
     * @return объект-ответ, содержащий название страницы, на которую должен будет осуществлен переход и http статус.
     * В случае ошибки возвращает объект-ответ-ошибки с помощью {@link AdviceController}
     */
    @GetMapping
    public ResponseEntity<?> register(@RequestParam String phoneCode, @RequestParam String phone,
                                      @RequestParam String password, @RequestParam String nickname,
                                      @RequestParam String role) {
        logger.info("Start registration process.");

        return new ResponseEntity<>("/chikaboom/" + registrationActionService
                .register(phoneCode, phone, password, nickname, role), HttpStatus.OK);
    }
}
