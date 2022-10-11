package net.chikaboom.controller.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PropertySource("/constants.properties")
public class HttpErrorController {

    @Value("${page.404}")
    private String ERROR404_PAGE;
    @Value("${page.500}")
    private String ERROR500_PAGE;

    @RequestMapping(value = "/400")
    public String error400() {
        return ERROR404_PAGE;
    }

    @RequestMapping(value = "/404")
    public String error404() {
        return ERROR404_PAGE;
    }

    @RequestMapping(value = "/500")
    public String error500() {
        return ERROR500_PAGE;
    }
}
