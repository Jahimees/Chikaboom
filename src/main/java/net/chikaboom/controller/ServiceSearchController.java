package net.chikaboom.controller;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Обрабатывает запросы по поиску мастера.
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/chikaboom/service")
public class ServiceSearchController {

    @Value("${page.service_search}")
    private String SERVICE_SEARCH_PAGE;
    @Value("${page.service_page}")
    private String SERVICE_PAGE;

    private final Logger logger = Logger.getLogger(this.getClass());

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
    public String getServiceSearchPage(@PathVariable int idServiceType) {
        logger.info("Loading search page with idServiceType " + idServiceType);

        return SERVICE_SEARCH_PAGE;
    }
}
