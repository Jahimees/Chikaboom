package net.chikaboom.controller.tab.service_tab;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.model.database.Subservice;
import net.chikaboom.model.database.UserService;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.tab.ServiceTabService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Обрабатывает запросы, связанные с вкладкой услуг
 */
@Controller
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
    private final Logger logger = Logger.getLogger(this.getClass());

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
        logger.info("Opening serviceTab");
        List<Subservice> subservices = serviceTabService.findAllSubservices();

        String subservicesJson = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            logger.info("Trying to convert subserviceList to JSON format");
            subservicesJson = mapper.writeValueAsString(subservices);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
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

    /**
     * Загружает информацию обо всех услугах, который создал пользователь (idAccount - идентифицирует пользователя).
     *
     * @param idAccount идентификатор аккаунта
     * @return полную информацию в формате JSON обо всех созданных пользователем услугах.
     */
    @GetMapping("/info")
    public ResponseEntity<String> loadUserServicesInfo(@PathVariable int idAccount) {
        logger.info("Getting full info about userServices of account with id " + idAccount);
        clientDataStorageService.setData(ID_ACCOUNT, idAccount);
        List<UserService> userServiceList = serviceTabService.executeAndGetList();

        logger.info("User (idAccount=" + idAccount + ") have " + userServiceList.size() + " userServices");

        ObjectMapper mapper = new ObjectMapper();
        String userServicesJson = "";
        try {
            logger.info("Trying to convert userServiceList to JSON format");
            userServicesJson = mapper.writeValueAsString(userServiceList);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
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
        logger.info("Creating or updating userService of user with id " + idAccount);
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
        logger.info("Deleting userService (idUserService=" + idUserService + ") of user with id " + idUserService);
        clientDataStorageService.setData(ID_USER_SERVICE, idUserService);

        serviceTabService.deleteUserService();

        clientDataStorageService.clearAllData();

//        TODO доделать возвращаемый элемент
        return new ResponseEntity<>("Dropper", HttpStatus.ACCEPTED);
    }
}
