package net.chikaboom.controller.tab.service_tab;

import lombok.RequiredArgsConstructor;
import net.chikaboom.service.tab.ServiceTabService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Обрабатывает запросы, связанные с вкладкой услуг
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/chikaboom/personality/{idAccount}/services")
public class ServiceTabController {

    @Value("${tab.serviceType}")
    private String SERVICE_TAB;
    @Value("${tab.serviceType.general}")
    private String GENERAL_SERVICE_TAB;

    private final ServiceTabService serviceTabService;
    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Загружает вкладку услуг и отправляет на неё данные о всех имеющихся услугах и подуслугах в распоряжении мастера
     *
     * @param idAccount идентификатор пользователя
     * @return представление вкладки и json всех подуслуг
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount and hasRole('MASTER')")
    @GetMapping
    public String openServiceTab(@PathVariable int idAccount) {
        return SERVICE_TAB;
    }

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
}
