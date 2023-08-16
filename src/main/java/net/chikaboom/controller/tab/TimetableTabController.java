package net.chikaboom.controller.tab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Отвечает за все запросы к календарю на странице графика
 */
@Controller
@RequestMapping
public class TimetableTabController {

    @Value("${tab.timetable}")
    private String TIMETABLE_TAB;
    @Value("${page.calendar}")
    private String CALENDAR_PAGE;

    /**
     * Загружает вкладку расписания для мастера.
     *
     * @param idAccount идентификатор мастера
     * @return путь к вкладке с расписанием
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount and hasRole('MASTER')")
    @GetMapping("/chikaboom/personality/{idAccount}/timetable")
    public String openTimetableTab(@PathVariable int idAccount) {
        return TIMETABLE_TAB;
    }

    /**
     * Загрузка календаря.
     *
     * @return путь к странице с календарем
     */
    @GetMapping("/chikaboom/personality/calendar")
    public String getCalendar() {
        return CALENDAR_PAGE;
    }
}
