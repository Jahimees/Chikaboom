package net.chikaboom.controller.service_tab;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.model.database.Subservice;
import net.chikaboom.model.database.UserService;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.tab.ServiceTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Перехватывает события отвечающие за вкладку услуг
 */
@Controller
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/personality/{idAccount}/services")
public class ServiceTabController {

    @Value("${tab.service}")
    private String SERVICE_TAB;
    @Value("${tab.service.general}")
    private String GENERAL_SERVICE_TAB;
    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.subservices}")
    private String SUBSERVICES;
    @Value("${attr.userService}")
    private String USER_SERVICE;
    @Value("${attr.idUserService}")
    private String ID_USER_SERVICE;

    private final ServiceTabService serviceTabService;
    private final ClientDataStorageService clientDataStorageService;

    @Autowired
    public ServiceTabController(ServiceTabService serviceTabService, ClientDataStorageService clientDataStorageService) {
        this.serviceTabService = serviceTabService;
        this.clientDataStorageService = clientDataStorageService;
    }

    /**
     * Загружает вкладку услуг и отправляет на неё данные о всех имеющихся услугах и подуслугах в распоряжении мастера
     *
     * @param idAccount идентификатор пользователя
     * @return представление вкладки и json всех подуслуг
     */
    @GetMapping
    public ModelAndView openServiceTab(@PathVariable int idAccount) {
        List<Subservice> subservices = serviceTabService.findAllSubservices();

        String subservicesJson = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            subservicesJson = mapper.writeValueAsString(subservices);
        } catch (JsonProcessingException e) {
//            TODO exception
            e.printStackTrace();
        }

        ModelAndView modelAndView = new ModelAndView(SERVICE_TAB);
        modelAndView.addObject(SUBSERVICES, subservicesJson);

        return modelAndView;
    }

    /**
     * Загружает основную вкладку услуг в личном кабинете мастера
     *
     * @param idAccount идентификатор мастера
     * @return json, содержащий все услуги, которые предоставляет мастер
     */
    @GetMapping("/general")
    public String loadGeneralServiceTab(@PathVariable int idAccount) {
        return GENERAL_SERVICE_TAB;
    }

    @GetMapping("/info")
    public ResponseEntity<String> loadUserServicesInfo(@PathVariable int idAccount) {
        clientDataStorageService.setData(ID_ACCOUNT, idAccount);
        List<UserService> userServiceList = serviceTabService.executeAndGetList();

        ObjectMapper mapper = new ObjectMapper();
        String userServicesJson = "";
        try {
            userServicesJson = mapper.writeValueAsString(userServiceList);
        } catch (JsonProcessingException e) {
//            TODO Exception
            e.printStackTrace();
        }

        clientDataStorageService.clearAllData();

        return new ResponseEntity<>(userServicesJson, HttpStatus.OK);
    }

    /**
     * Создает либо обновляет данные об услуге мастера
     *
     * @param idAccount   идентификатор мастера
     * @param userService услуга, которую предоставляет мастер
     * @return обновленная услуга
     */
    @PostMapping
    public ResponseEntity<UserService> createOrUpdateUserService(@PathVariable int idAccount, @RequestBody UserService userService) {
        clientDataStorageService.setData(USER_SERVICE, userService);

        UserService userServiceToSend = serviceTabService.saveUserService();

        clientDataStorageService.clearAllData();

        return new ResponseEntity<>(userServiceToSend, HttpStatus.CREATED);
    }

    /**
     * Удаляет из базы данных указанную услугу
     *
     * @param idAccount     идентификатор мастера
     * @param idUserService идентификатор услуги, которую необходимо удалить
     * @return строку, содержащую результат удаления TODO
     */
    @DeleteMapping("/{idUserService}")
    public ResponseEntity<String> deleteUserService(@PathVariable int idAccount, @PathVariable int idUserService) {
        clientDataStorageService.setData(ID_USER_SERVICE, idUserService);

        serviceTabService.deleteUserService();

        clientDataStorageService.clearAllData();

//        TODO доделать возвращаемый элемент
        return new ResponseEntity<>("Dropper", HttpStatus.ACCEPTED);
    }
}
