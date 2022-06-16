package net.chikaboom.controller;

import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.RegistrationActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static net.chikaboom.util.constant.RequestParametersConstant.EMAIL;
import static net.chikaboom.util.constant.RequestParametersConstant.PASSWORD;

/**
 * Класс-контроллер, отвечающий за регистрацию пользователя.
 * <p>
 * Контроллер сохраняет данные запроса в {@link ClientDataStorageService} и передает управление в {@link RegistrationActionService}
 */
@Controller
@RequestMapping("/chikaboom/registration")
public class RegistrationController {

    private final RegistrationActionService registrationActionService;
    private final ClientDataStorageService clientDataStorageService;

    @Autowired
    public RegistrationController(RegistrationActionService registrationActionService, ClientDataStorageService clientDataStorageService) {
        this.registrationActionService = registrationActionService;
        this.clientDataStorageService = clientDataStorageService;
    }

    /**
     * Сохраняет параметры клиента и передает управление в сервис {@link RegistrationActionService}
     *
     * @param email    параметр электронной почты
     * @param password параметр пароля
     * @return объект-ответ, содержащий название страницы, на которую должен будет осуществлен переход и http статус.
     * В случае ошибки возвращает объект-ответ-ошибки с помощью {@link AdviceController}
     */
    @GetMapping
    public ResponseEntity<?> register(@RequestParam String email, @RequestParam String password) {
        clientDataStorageService.setData(EMAIL, email);
        clientDataStorageService.setData(PASSWORD, password);

        return new ResponseEntity<Object>("/chikaboom/" + registrationActionService.execute(), HttpStatus.OK);
    }
}
