package net.chikaboom.controller.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер предназначен для маппинга страниц ошибок.
 */
@Controller
@PreAuthorize("permitAll()")
public class HttpErrorController {

    @Value("${page.400}")
    private String ERROR400_PAGE;
    @Value("${page.403}")
    private String ERROR403_PAGE;
    @Value("${page.404}")
    private String ERROR404_PAGE;
    @Value("${page.500}")
    private String ERROR500_PAGE;

    /**
     * Возвращает страницу, соответствующую http коду 400.
     *
     * @return путь к странице
     */
    @GetMapping(value = "/400")
    public String error400() {
        return ERROR400_PAGE;
    }

    /**
     * Возвращает страницу, соответствующую http коду 403.
     *
     * @return путь к странице
     */
    @GetMapping(value = "/403")
    public String error403() {
        return ERROR403_PAGE;
    }

    /**
     * Возвращает страницу, соответствующую http коду 404.
     *
     * @return путь к странице
     */
    @GetMapping(value = "/404")
    public String error404() {
        return ERROR404_PAGE;
    }

    /**
     * Возвращает страницу, соответствующую http коду 500.
     *
     * @return путь к странице
     */
    @GetMapping(value = "/500")
    public String error500() {
        return ERROR500_PAGE;
    }
}
