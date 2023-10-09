package net.chikaboom.facade.converter;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.WorkingDayFacade;
import net.chikaboom.model.database.WorkingDay;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class WorkingDayFacadeConverter implements FacadeConverter<WorkingDayFacade, WorkingDay> {

    private final AccountFacadeConverter accountFacadeConverter;

    @Override
    public WorkingDayFacade convertToDto(WorkingDay model) {
        WorkingDayFacade workingDayFacade = new WorkingDayFacade();

        workingDayFacade.setIdWorkingDay(model.getIdWorkingDay());
        workingDayFacade.setAccountFacade(accountFacadeConverter.convertToDto(model.getAccount()));
        workingDayFacade.setDate((Timestamp) model.getDate().clone());
        workingDayFacade.setWorkingDayStart((Time) model.getWorkingDayStart().clone());
        workingDayFacade.setWorkingDayEnd((Time) model.getWorkingDayEnd().clone());

        return workingDayFacade;
    }

    @Override
    public WorkingDay convertToModel(WorkingDayFacade facade) {
        WorkingDay workingDay = new WorkingDay();

        workingDay.setIdWorkingDay(facade.getIdWorkingDay());
        workingDay.setAccount(accountFacadeConverter.convertToModel(facade.getAccountFacade()));
        workingDay.setDate((Timestamp) facade.getDate().clone());
        workingDay.setWorkingDayStart((Time) facade.getWorkingDayStart().clone());
        workingDay.setWorkingDayEnd((Time) facade.getWorkingDayEnd().clone());

        return workingDay;
    }
}
