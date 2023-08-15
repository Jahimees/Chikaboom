package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.WorkingDays;
import net.chikaboom.service.tab.TimetableTabService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST контроллер для взаимодействия с сущностями типа {@link WorkingDays}
 */
@RequiredArgsConstructor
@RestController
public class WorkingDaysRestController {

    private final TimetableTabService timetableTabService;

    @GetMapping("/accounts/{idAccount}/workingDays")
    public ResponseEntity<WorkingDays> findWorkingDaysByIdAccount(@PathVariable int idAccount) {
        return ResponseEntity.ok(timetableTabService.findWorkingDaysByIdAccount(idAccount));
    }

    /**
     * Обрабатывает запрос обновления данных о рабочих днях на странице и передает управление в {@link TimetableTabService}
     *
     * @param idAccount   идентификатор мастера
     * @param workingDays объект рабочих дней
     * @return обновленный json объект
     */
    @PutMapping("/accounts/{idAccount}/workingDays")
    public ResponseEntity<WorkingDays> updateWorkingDaysForAccount(@PathVariable int idAccount,
                                                                   @RequestBody WorkingDays workingDays) {

        return ResponseEntity.ok(timetableTabService.updateWorkingDays(idAccount, workingDays));
    }
}
