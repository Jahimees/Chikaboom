package net.chikaboom.controller.tab;

import net.chikaboom.annotation.LoggableViewController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chikaboom/personality/{idAccount}")
public class AnalyticsTabController {

    @Value("${tab.analytics}")
    private String ANALYTICS_TAB;
    @Value("${tab.analytics.appointment}")
    private String APPOINTMENT_ANALYTICS_TAB;
    @Value("${tab.analytics.client}")
    private String CLIENT_ANALYTICS_TAB;

    @LoggableViewController
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    @GetMapping(value = "/analytics")
    public String openAnalyticsTab(@PathVariable int idAccount) {
        return ANALYTICS_TAB;
    }

    @LoggableViewController
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    @GetMapping(value = "/analytics/appointment")
    public String openAppointmentAnalyticsTab(@PathVariable int idAccount) {
        return APPOINTMENT_ANALYTICS_TAB;
    }

    @LoggableViewController
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    @GetMapping(value = "/analytics/client")
    public String openClientAnalyticsTab(@PathVariable int idAccount) {
        return CLIENT_ANALYTICS_TAB;
    }
}
