package net.chikaboom.controller;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Обрабатывает запросы по поиску мастера.
 */
@RestController
@RequestMapping("/chikaboom/service")
public class ServiceSearchController {

    @Value("${page.service_search}")
    private String SERVICE_SEARCH_PAGE;
    @Value("${page.service_page}")
    private String SERVICE_PAGE;
    @Value("${attr.idService}")
    private String ID_SERVICE;
    @Value("${attr.subserviceList}")
    private String SUBSERVICE_LIST;
    @Value("${attr.subserviceIdList}")
    private String SUBSERVICE_ID_LIST;

    private final ClientDataStorageService clientDataStorageService;
    private final ServiceTabService serviceTabService;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public ServiceSearchController(ClientDataStorageService clientDataStorageService, ServiceTabService serviceTabService) {
        this.clientDataStorageService = clientDataStorageService;
        this.serviceTabService = serviceTabService;
    }


    /**
     * Загружает страницу со всеми услугами
     *
     * @return пустую модель и представление страницы услуг
     */
    @GetMapping
    public ModelAndView getServicePage() {
        return new ModelAndView(SERVICE_PAGE);
    }

    /**
     * Загружает все подуслуги, выбранной услуги для дальнейшего поиска
     *
     * @param idService идентификатор услуги
     * @return модель с данными подуслуг и представление страницы поиска мастеров
     */
    @GetMapping(value = "/search/{idService}")
    public ModelAndView getServiceSearchPage(@PathVariable int idService) {
        logger.info("Loading subservices with idService " + idService);
        clientDataStorageService.setData(ID_SERVICE, idService);

        List<Subservice> subserviceList = serviceTabService.getSubservices();

        clientDataStorageService.clearAllData();

        ModelAndView modelAndView = new ModelAndView(SERVICE_SEARCH_PAGE);
        modelAndView.addObject(SUBSERVICE_LIST, subserviceList);

        return modelAndView;
    }

    /**
     * Осуществляет непосредственно поиск по выбранным подуслугам. Если ни одна подуслуга не была выбрана, поиск ведется
     * по всем возможным подуслугам.
     *
     * @param idService        идентификатор услуги
     * @param subserviceIdList набор идентификаторов подуслуг в формате JSON
     * @return данные о пользовательских услугах, отвечающие заданным параметрам поиска
     */
    @GetMapping(value = "/search/{idService}/dosearch")
    public ResponseEntity<String> getUserServicesBySubserviceIds(@PathVariable int idService, @RequestParam String subserviceIdList) {
        logger.info("Searching userServices by subserviceIdList");

        Integer[] subserviceIdArr = new Integer[0];
        try {
            logger.info("Trying to parse subserviceIdList");
            subserviceIdArr = new ObjectMapper().readValue(subserviceIdList, Integer[].class);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        clientDataStorageService.setData(SUBSERVICE_ID_LIST, subserviceIdArr);
        clientDataStorageService.setData(ID_SERVICE, idService);

        List<UserService> userServiceList = serviceTabService.getUserServicesByServiceIds();
        String result = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            logger.info("Trying to convert userServiceList to JSON format");
            result = mapper.writeValueAsString(userServiceList);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        clientDataStorageService.clearAllData();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
