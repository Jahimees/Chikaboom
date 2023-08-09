package net.chikaboom.controller.tab.service_tab;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.model.database.Service;
import net.chikaboom.model.database.ServiceSubtype;
import net.chikaboom.service.tab.ServiceTabService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Value("${tab.serviceType}")
    private String SERVICE_TAB;
    @Value("${tab.serviceType.general}")
    private String GENERAL_SERVICE_TAB;
    @Value("${attr.service_subtypes}")
    private String SERVICE_SUBTYPES;

    private final ServiceTabService serviceTabService;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public ServiceTabController(ServiceTabService serviceTabService) {
        this.serviceTabService = serviceTabService;
    }

    /**
     * Загружает вкладку услуг и отправляет на неё данные о всех имеющихся услугах и подуслугах в распоряжении мастера
     *
     * @param idAccount идентификатор пользователя
     * @return представление вкладки и json всех подуслуг
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount and hasRole('MASTER')")
    @GetMapping
    public ModelAndView openServiceTab(@PathVariable int idAccount) {
        logger.info("Opening serviceTab");
        List<ServiceSubtype> serviceSubtypes = serviceTabService.findAllServiceSubtypesByIdServiceType();

        String serviceSubtypesJson = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            logger.info("Trying to convert serviceSubtypeList to JSON format");
            serviceSubtypesJson = mapper.writeValueAsString(serviceSubtypes);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        ModelAndView modelAndView = new ModelAndView(SERVICE_TAB);
        modelAndView.addObject(SERVICE_SUBTYPES, serviceSubtypesJson);

        return modelAndView;
    }

//    TODO NEW что почему и как? Слить с loadUserServicesInfo

    /**
     * Загружает основную вкладку услуг в личном кабинете мастера
     *
     * @param idAccount идентификатор мастера
     * @return json, содержащий все услуги, которые предоставляет мастер
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
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
    @PreAuthorize("permitAll()")
    @GetMapping("/info")
    public ResponseEntity<String> loadServicesInfo(@PathVariable int idAccount) {
        logger.info("Getting full info about services of account with id " + idAccount);
        List<Service> serviceList = serviceTabService.findAllServicesByIdAccount(idAccount);

        logger.info("User (idAccount=" + idAccount + ") have " + serviceList.size() + " services");

        ObjectMapper mapper = new ObjectMapper();
        String servicesJson = "";
        try {
            logger.info("Trying to convert serviceList to JSON format");
            servicesJson = mapper.writeValueAsString(serviceList);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        return new ResponseEntity<>(servicesJson, HttpStatus.OK);
    }

    /**
     * Создает либо обновляет данные об услуге мастера
     *
     * @param idAccount   идентификатор мастера
     * @param service услуга, которую предоставляет мастер
     * @return обновленная услуга
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount and hasRole('MASTER')")
    @PostMapping
    public ResponseEntity<Service> createOrUpdateService(@PathVariable int idAccount, @RequestBody Service service) {
        logger.info("Creating or updating service of user with id " + idAccount);

        Service serviceToSend = serviceTabService.saveService(service);

        return new ResponseEntity<>(serviceToSend, HttpStatus.CREATED);
    }

    /**
     * Удаляет из базы данных указанную услугу
     *
     * @param idAccount идентификатор мастера
     * @param idService идентификатор услуги, которую необходимо удалить
     * @return строку, содержащую результат удаления
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount and hasRole('MASTER')")
    @DeleteMapping("/{idService}")
    public ResponseEntity<String> deleteService(@PathVariable int idAccount, @PathVariable int idService) {
        logger.info("Deleting service (idService=" + idService + ")");

        serviceTabService.deleteService(idService);

//        TODO доделать возвращаемый элемент
        return new ResponseEntity<>("Dropper", HttpStatus.ACCEPTED);
    }
}
