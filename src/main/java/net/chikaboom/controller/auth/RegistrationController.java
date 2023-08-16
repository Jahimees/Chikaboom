package net.chikaboom.controller.auth;

import lombok.RequiredArgsConstructor;
import net.chikaboom.controller.error.AdviceController;
import net.chikaboom.model.database.Account;
import net.chikaboom.service.RegistrationActionService;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Класс-контроллер, отвечающий за регистрацию пользователя.
 * <p>
 * Контроллер передает управление в {@link RegistrationActionService}
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/chikaboom/registration")
public class RegistrationController {

    private final RegistrationActionService registrationActionService;
    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Передает управление и данные в сервис {@link RegistrationActionService}
     * В случае ошибки возвращает объект-ответ-ошибки с помощью {@link AdviceController}
     *
     * @param account объект регистрируемого аккаунта
     * @return объект-ответ, содержащий название страницы, на которую должен будет осуществлен переход и http статус.
     */
    @PostMapping
    public ResponseEntity<?> register(@RequestBody Account account) {
        logger.info("Start registration process.");

        return ResponseEntity.ok(registrationActionService.register(account));
    }
}
