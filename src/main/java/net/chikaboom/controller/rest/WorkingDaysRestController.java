package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.WorkingDay;
import net.chikaboom.service.data.AccountDataService;
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

    @GetMapping("/accounts/{idAccount}/working-days")
    public ResponseEntity<List<WorkingDay>> findWorkingDaysByIdAccount(@PathVariable int idAccount) {
        return ResponseEntity.ok(workingDayDataService.findWorkingDaysByIdAccount(idAccount));
    }

    @PostMapping("/accounts/{idAccount}/working-days")
    @PreAuthorize("hasRole('MASTER') and #idAccount == authentication.principal.idAccount")
    public ResponseEntity<WorkingDay> createWorkingDayForAccount(@PathVariable int idAccount,
                                                                 @RequestBody WorkingDay workingDay) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        workingDay.setAccount(accountOptional.get());

        return ResponseEntity.ok(workingDayDataService.create(workingDay));
    }

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
