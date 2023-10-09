package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.AccountSettings;
import net.chikaboom.model.database.WorkingDay;
import net.chikaboom.service.data.AccountDataService;
import net.chikaboom.service.data.AccountSettingsDataService;
import net.chikaboom.service.data.WorkingDayDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<WorkingDay>> findWorkingDaysByIdAccount(@PathVariable int idAccount) {
        return ResponseEntity.ok(workingDayDataService.findWorkingDaysByIdAccount(idAccount));
    }

    /**
     * Создает рабочий день мастера
     *
     * @param idAccount  идентификатор аккаунта, на котором создается рабочий день
     * @param workingDay рабочий день, который необходимо сохранить в базе данных
     * @return созданный рабочий день
     */
    @PreAuthorize("hasRole('MASTER') and #idAccount == authentication.principal.idAccount")
    @PostMapping("/accounts/{idAccount}/working-days")
    public ResponseEntity<WorkingDay> createWorkingDayForAccount(@PathVariable int idAccount,
                                                                 @RequestBody WorkingDay workingDay) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        workingDay.setAccount(accountOptional.get());

        Optional<AccountSettings> accountSettings = accountSettingsDataService.findByIdAccount(idAccount);

        if (!accountSettings.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (workingDay.getWorkingDayStart() == null) {
            workingDay.setWorkingDayStart(accountSettings.get().getDefaultWorkingDayStart());
        }

        if (workingDay.getWorkingDayEnd() == null) {
            workingDay.setWorkingDayEnd(accountSettings.get().getDefaultWorkingDayEnd());
        }

        return ResponseEntity.ok(workingDayDataService.create(workingDay));
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
    public ResponseEntity<String> deleteWorkingDay(@PathVariable int idAccount, @PathVariable int idWorkingDay) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (!workingDayDataService.isWorkingDayExists(idWorkingDay)) {
            return ResponseEntity.notFound().build();
        }

        workingDayDataService.deleteById(idWorkingDay);
        return ResponseEntity.ok().build();
    }
}
