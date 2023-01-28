package net.chikaboom.controller.tab.timetable_tab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Загрузка календаря
 */
@Controller
@RequestMapping("/chikaboom/calendar")
public class CalendarController {

    @Value("${page.calendar}")
    private String CALENDAR_PAGE;

    /**
     * Загрузка календаря.
     *
     * @return путь к странице с календарем
     */
    @GetMapping
    public String getCalendar() {
        return CALENDAR_PAGE;
    }
}
