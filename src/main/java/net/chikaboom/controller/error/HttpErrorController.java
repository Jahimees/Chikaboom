package net.chikaboom.controller.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер предназначен для передачи данных ошибок на клиент, либо переадресации на страницу ошибки.
 */
@Controller
public class HttpErrorController {

    @Value("${page.404}")
    private String ERROR404_PAGE;
    @Value("${page.500}")
    private String ERROR500_PAGE;

    /**
     * Возвращает страницу, соответствующую http коду 400.
     *
     * @return путь к странице
     */
    @RequestMapping(value = "/400")
    public String error400() {
        return ERROR404_PAGE;
    }

    /**
     * Возвращает страницу, соответствующую http коду 404.
     *
     * @return путь к странице
     */
    @RequestMapping(value = "/404")
    public String error404() {
        return ERROR404_PAGE;
    }

    /**
     * Возвращает страницу, соответствующую http коду 500.
     *
     * @return путь к странице
     */
    @RequestMapping(value = "/500")
    public String error500() {
        return ERROR500_PAGE;
    }
}
