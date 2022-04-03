package net.chikaboom.controller;

import net.chikaboom.service.action.RegistrationActionService;
import net.chikaboom.service.ClientDataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static net.chikaboom.util.constant.RequestParametersConstant.EMAIL;
import static net.chikaboom.util.constant.RequestParametersConstant.PASSWORD;

/**
 * Класс-контроллер, отвечающий за регистрацию пользователя.
 *
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
     * Сохранение данных и передача управления в сервис
     *
     * @param email параметр электронной почты
     * @param password параметр пароля
     * @return название страницы, на которую будет переведен пользователь
     */
    @PostMapping
    public String register(@RequestParam String email, @RequestParam String password) {
        clientDataStorageService.setData(EMAIL, email);
        clientDataStorageService.setData(PASSWORD, password);

        return registrationActionService.execute();
    }
}
