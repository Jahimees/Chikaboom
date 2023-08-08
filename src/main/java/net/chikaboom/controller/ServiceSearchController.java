package net.chikaboom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.model.database.Service;
import net.chikaboom.model.database.ServiceSubtype;
import net.chikaboom.service.action.tab.ServiceTabService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Value("${attr.serviceSubtypeList}")
    private String SERVICE_SUBTYPE_LIST;

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
    @PreAuthorize("permitAll()")
    @GetMapping
    public ModelAndView getServicePage() {
        return new ModelAndView(SERVICE_PAGE);
    }

    /**
     * Загружает все подуслуги, выбранной услуги для дальнейшего поиска
     *
     * @param idServiceType идентификатор типа услуги
     * @return модель с данными подуслуг и представление страницы поиска мастеров
     */
    @PreAuthorize("permitAll()")
    @GetMapping(value = "/search/{idServiceType}")
    public ModelAndView getServiceSearchPage(@PathVariable int idServiceType) {
        logger.info("Loading service subtypes with idServiceType " + idServiceType);

        List<ServiceSubtype> serviceSubtypeList = serviceTabService.findAllServiceSubtypesByIdServiceType(idServiceType);

        ModelAndView modelAndView = new ModelAndView(SERVICE_SEARCH_PAGE);
        modelAndView.addObject(SERVICE_SUBTYPE_LIST, serviceSubtypeList);

        return modelAndView;
    }

    /**
     * Осуществляет непосредственно поиск по выбранным подуслугам. Если ни одна подуслуга не была выбрана, поиск ведется
     * по всем возможным подуслугам.
     *
     * @param idServiceType            идентификатор услуги
     * @param serviceSubtypeIdListJson набор идентификаторов подуслуг в формате JSON
     * @return данные о пользовательских услугах, отвечающие заданным параметрам поиска
     */
    @PreAuthorize("permitAll()")
    @GetMapping(value = "/search/{idServiceType}/dosearch")
    public ResponseEntity<String> getServicesByServiceSubtypeIds(@PathVariable int idServiceType, @RequestParam String serviceSubtypeIdListJson) {
        logger.info("Searching services by serviceSubtypeIds");

        int[] serviceSubtypeIdArray = new int[0];
        try {
            logger.info("Trying to parse serviceSubtypeIdList");
            serviceSubtypeIdArray = new ObjectMapper().readValue(serviceSubtypeIdListJson, int[].class);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        List<Service> serviceList = serviceTabService.getServicesByServiceSubtypeIds(serviceSubtypeIdArray, idServiceType);
        String result = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            logger.info("Trying to convert serviceList to JSON format");
            result = mapper.writeValueAsString(serviceList);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
