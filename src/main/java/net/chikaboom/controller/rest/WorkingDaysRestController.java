package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.AccountSettingsFacade;
import net.chikaboom.facade.dto.Facade;
import net.chikaboom.facade.dto.WorkingDayFacade;
import net.chikaboom.model.database.WorkingDay;
import net.chikaboom.model.response.CustomResponseObject;
import net.chikaboom.service.data.AccountDataService;
import net.chikaboom.service.data.AccountSettingsDataService;
import net.chikaboom.service.data.WorkingDayDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для взаимодействия с сущностями типа {@link WorkingDay}
 */
@RequiredArgsConstructor
@RestController
public class WorkingDaysRestController {

    private final AccountDataService accountDataService;
    private final WorkingDayDataService workingDayDataService;
    private final AccountSettingsDataService accountSettingsDataService;

    /**
     * Предоставляет список рабочих дней выбранного аккаунта
     *
     * @param idAccount идентификатор аккаунта, по которому производится поиск рабочих дней
     * @return список рабочих дней мастера
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/accounts/{idAccount}/working-days")
    public ResponseEntity<List<WorkingDayFacade>> findWorkingDaysByIdAccount(@PathVariable int idAccount) {
        return ResponseEntity.ok(workingDayDataService.findWorkingDaysByIdAccount(idAccount));
    }

    /**
     * Создает рабочий день мастера
     *
     * @param idAccount        идентификатор аккаунта, на котором создается рабочий день
     * @param workingDayFacade рабочий день, который необходимо сохранить в базе данных
     * @return созданный рабочий день
     */
    @PreAuthorize("hasRole('MASTER') and #idAccount == authentication.principal.idAccount")
    @PostMapping("/accounts/{idAccount}/working-days")
    public ResponseEntity<WorkingDayFacade> createWorkingDayForAccount(@PathVariable int idAccount,
                                                                       @RequestBody WorkingDayFacade workingDayFacade) {
        AccountFacade accountFacade = accountDataService.findById(idAccount);

        workingDayFacade.setAccountFacade(accountFacade);

        AccountSettingsFacade accountSettingsFacade = accountSettingsDataService.findByIdAccount(idAccount);

        if (workingDayFacade.getWorkingDayStart() == null) {
            workingDayFacade.setWorkingDayStart(accountSettingsFacade.getDefaultWorkingDayStart());
        }

        if (workingDayFacade.getWorkingDayEnd() == null) {
            workingDayFacade.setWorkingDayEnd(accountSettingsFacade.getDefaultWorkingDayEnd());
        }

        return ResponseEntity.ok(workingDayDataService.create(workingDayFacade));
    }

    /**
     * Удаляет рабочий день мастера из базы данных
     *
     * @param idAccount    идентификатор аккаунта, чей рабочий день удаляется
     * @param idWorkingDay идентификатор удаляемого рабочего дня
     * @return http ответ
     */
    @PreAuthorize("hasRole('MASTER') && #idAccount == authentication.principal.idAccount")
    @DeleteMapping("/accounts/{idAccount}/working-days/{idWorkingDay}")
    public ResponseEntity<Facade> deleteWorkingDay(@PathVariable int idAccount, @PathVariable int idWorkingDay) {
        if (accountDataService.isAccountExistsById(idAccount)) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.NOT_EXTENDED.value(),
                    "There not found account with id " + idAccount,
                    "DELETE:/accounts/" + idAccount + "/working-days/" + idWorkingDay
            ), HttpStatus.NOT_FOUND);
        }

        if (!workingDayDataService.isWorkingDayExists(idWorkingDay)) {

            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.NOT_FOUND.value(),
                    "There not found deletion working day. Please check the idWorkingDay " + idWorkingDay,
                    "DELETE:/accounts/" + idAccount + "/working-days/" + idWorkingDay
            ), HttpStatus.NOT_FOUND);
        }

        workingDayDataService.deleteById(idWorkingDay);
        return ResponseEntity.ok(new CustomResponseObject(
                HttpStatus.OK.value(),
                "WorkingDay with id " + idWorkingDay + " deleted",
                "DELETE:/accounts/" + idAccount + "/working-days/" + idWorkingDay
        ));
    }
}
