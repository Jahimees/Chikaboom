package net.chikaboom.controller.tab.service_tab;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Отвечает за отрисовку вкладки услуг
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/chikaboom/personality/{idAccount}/services")
public class ServiceTabController {

    @Value("${tab.serviceType}")
    private String SERVICE_TAB;
    @Value("${tab.serviceType.general}")
    private String GENERAL_SERVICE_TAB;

    /**
     * Загружает вкладку услуг
     *
     * @param idAccount идентификатор пользователя
     * @return путь к вкладке услуг
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount and hasRole('MASTER')")
    @GetMapping
    public String openServiceTab(@PathVariable int idAccount) {
        return SERVICE_TAB;
    }

    /**
     * Загружает основную подвкладку услуг в личном кабинете мастера
     *
     * @param idAccount идентификатор мастера
     * @return путь к основной подвкладке услуг
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    @GetMapping("/general")
    public String loadGeneralServiceTab(@PathVariable int idAccount) {
        return GENERAL_SERVICE_TAB;
    }
}
