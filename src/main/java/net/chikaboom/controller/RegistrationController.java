package net.chikaboom.controller;

import net.chikaboom.controller.error.AdviceController;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.RegistrationActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Класс-контроллер, отвечающий за регистрацию пользователя.
 * <p>
 * Контроллер сохраняет данные запроса в {@link ClientDataStorageService} и передает управление в {@link RegistrationActionService}
 */
@Controller
@RequestMapping("/chikaboom/registration")
public class RegistrationController {

    @Value("${attr.phone}")
    private String PHONE;
    @Value("${attr.phoneCode}")
    private String PHONE_CODE;
    @Value("${attr.password}")
    private String PASSWORD;
    @Value("${attr.nickname}")
    private String NICKNAME;
    @Value("${attr.role}")
    private String ROLE;

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
        clientDataStorageService.setData(PHONE_CODE, phoneCode);
        clientDataStorageService.setData(PHONE, phone);
        clientDataStorageService.setData(PASSWORD, password);
        clientDataStorageService.setData(NICKNAME, nickname);
        clientDataStorageService.setData(ROLE, role);

        return new ResponseEntity<Object>("/chikaboom/" + registrationActionService.execute(), HttpStatus.OK);
    }
}
