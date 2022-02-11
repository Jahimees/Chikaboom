package net.chikaboom.controller;

import net.chikaboom.service.action.RegistrationService;
import net.chikaboom.service.ClientDataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Класс-контроллер, отвечающий за регистрацию пользователя.
 *
 * Контроллер сохраняет данные запроса в {@link ClientDataStorageService} и передает управление в {@link RegistrationService}
 */
@Controller
@RequestMapping("/chikaboom/registration")
public class RegistrationController {

    private RegistrationService registrationService;
    private ClientDataStorageService clientDataStorageService;

    @Autowired
    public RegistrationController(RegistrationService registrationService, ClientDataStorageService clientDataStorageService) {
        this.registrationService = registrationService;
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
        clientDataStorageService.setData("email", email);
        clientDataStorageService.setData("password", password);

        return registrationService.execute();
    }
}
