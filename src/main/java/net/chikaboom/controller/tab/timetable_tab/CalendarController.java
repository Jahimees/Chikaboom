package net.chikaboom.controller.tab.timetable_tab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер загружает сам календарь
 */
@Controller
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/calendar")
public class CalendarController {

    @Value("${page.calendar}")
    private String CALENDAR_PAGE;

    @GetMapping
    public String getCalendar() {
        return CALENDAR_PAGE;
    }
}
