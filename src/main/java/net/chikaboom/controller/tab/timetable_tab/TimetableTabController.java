package net.chikaboom.controller.tab.timetable_tab;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.model.database.WorkingDays;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.tab.TimetableTabService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Отвечает за все запросы к календарю на странице графика
 */
@Controller
@RequestMapping("/chikaboom/personality/{idAccount}/timetable")
public class TimetableTabController {

    @Value("${tab.timetable}")
    private String TIMETABLE_TAB;
    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.workingDays}")
    private String WORKING_DAYS;

    private final ClientDataStorageService clientDataStorageService;
    private final TimetableTabService timetableTabService;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public TimetableTabController(ClientDataStorageService clientDataStorageService, TimetableTabService timetableTabService) {
        this.clientDataStorageService = clientDataStorageService;
        this.timetableTabService = timetableTabService;
    }

    /**
     * Загружает вкладку расписания для мастера.
     * С помощью сервиса {@link TimetableTabService} загружает все данные о рабочих днях мастера, содержащие информацию
     * о том, какие дни являются рабочими, а также о начале и конце рабочего дня
     *
     * @param idAccount идентификатор мастера
     * @return модель с данными о рабочих днях и представление вкладки расписания
     */
    @GetMapping
    public ModelAndView openTimetableTab(@PathVariable int idAccount) {
        logger.info("Loading timetableTab.");
        ModelAndView modelAndView = new ModelAndView(TIMETABLE_TAB);
        clientDataStorageService.setData(ID_ACCOUNT, idAccount);

        WorkingDays workingDays = timetableTabService.getWorkingDays();

        clientDataStorageService.clearAllData();

        ObjectMapper mapper = new ObjectMapper();

        String workingDaysJSON = "";
        try {
            logger.info("Trying to convert workingDays to JSON format");
            if (workingDays != null) {
                workingDaysJSON = mapper.writeValueAsString(workingDays);
            } else {
                workingDaysJSON = mapper.writeValueAsString(new WorkingDays());
            }
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        modelAndView.addObject(WORKING_DAYS, workingDaysJSON);

        return modelAndView;
    }

    /**
     * Обрабатывает запрос обновления данных о рабочих днях на странице и передает управление в {@link TimetableTabService}
     *
     * @param idAccount   идентификатор мастера
     * @param workingDays объект рабочих дней
     * @return объект рабочих дней с HTTP статусом 200
     */
    @PostMapping
    public ResponseEntity<WorkingDays> updateWorkingDays(@PathVariable int idAccount,
                                                         @RequestBody WorkingDays workingDays) {
        logger.info("Updating workingDays data of user with id " + idAccount);
        clientDataStorageService.setData(ID_ACCOUNT, idAccount);
        clientDataStorageService.setData(WORKING_DAYS, workingDays);

        WorkingDays workingDaysObj = timetableTabService.executeAndGetOne();

        clientDataStorageService.clearAllData();

        return new ResponseEntity<>(workingDaysObj, HttpStatus.OK);
    }
}
