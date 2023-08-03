package net.chikaboom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.model.database.Subservice;
import net.chikaboom.model.database.UserService;
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
    @Value("${attr.subserviceList}")
    private String SUBSERVICE_LIST;

    private final ServiceTabService serviceTabService;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public ServiceSearchController(ServiceTabService serviceTabService) {
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

        List<Subservice> subserviceList = serviceTabService.findAllSubservices(idService);

        ModelAndView modelAndView = new ModelAndView(SERVICE_SEARCH_PAGE);
        modelAndView.addObject(SUBSERVICE_LIST, subserviceList);

        return modelAndView;
    }

    /**
     * Осуществляет непосредственно поиск по выбранным подуслугам. Если ни одна подуслуга не была выбрана, поиск ведется
     * по всем возможным подуслугам.
     *
     * @param idService             идентификатор услуги
     * @param subserviceIdArrayJSON набор идентификаторов подуслуг в формате JSON
     * @return данные о пользовательских услугах, отвечающие заданным параметрам поиска
     */
    @GetMapping(value = "/search/{idService}/dosearch")
    public ResponseEntity<String> getUserServicesBySubserviceIds(@PathVariable int idService, @RequestParam String subserviceIdArrayJSON) {
        logger.info("Searching userServices by subserviceIdArray");

        int[] subserviceIdArray = new int[0];
        try {
            logger.info("Trying to parse subserviceIdArray");
            subserviceIdArray = new ObjectMapper().readValue(subserviceIdArrayJSON, int[].class);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        List<UserService> userServiceList = serviceTabService.getUserServicesByServiceIds(subserviceIdArray, idService);
        String result = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            logger.info("Trying to convert userServiceList to JSON format");
            result = mapper.writeValueAsString(userServiceList);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
